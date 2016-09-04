/**************************************************************************
 TexTra Machine Translation plugin for OmegaT(http://www.omegat.org/)

 Copyright 2016,  Hiroshi Miura

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 **************************************************************************/

package tokyo.northside.omegat.textra;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.omegat.core.Core;
import org.omegat.core.machinetranslators.BaseTranslate;
import org.omegat.gui.exttrans.IMachineTranslation;
import org.omegat.util.Language;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


/**
 * Support TexTra powered by NICT machine translation.
 *
 * @author Hiroshi Miura
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OmegatTextraMachineTranslation extends BaseTranslate implements IMachineTranslation {
    protected static final String API_URL = "https://mt-auto-minhon-mlt.ucri.jgn-x.jp/api/mt/";
    private static final int CONNECTION_TIMEOUT = 2 * 60 * 1000;
    private static final int SO_TIMEOUT = 10 * 60 * 1000;

    @Override
    protected String getPreferenceName() {
        return "allow_textra_translate";
    }

    /**
     * Return MT name.
     * @return MT engine name.
     */
    public String getName() {
        // TexTra service terms demand to show "Powered by NICT" on every application screen.
        // You may keep it to be compliant with TexTra terms.
        // Because it is showed on MT pane.
        return "TexTra Powered by NICT";
    }

    /**
     * Register plugin into OmegaT.
     */
    public static void loadPlugins() {
        Core.registerMachineTranslationClass(OmegatTextraMachineTranslation.class);
    }

    public static void unloadPlugins() {
    }

    @Override
    protected String translate(final Language sLang, final Language tLang, final String text)
            throws Exception {

        if (System.getProperty("textra.api.username") == null
                || System.getProperty("textra.api.key") == null
                || System.getProperty("textra.api.secret") == null
                || System.getProperty("textra.api.engine") == null) {
            throw new Exception("TexTra API key/secret not found.");
        }
        String apiEngine = System.getProperty("textra.api.engine");

        String sourceLang = sLang.getLanguageCode();
        String targetLang = tLang.getLanguageCode();
        String apiUrl = getAccessUrl(apiEngine, sourceLang, targetLang);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SO_TIMEOUT)
                .build();
        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
        return requestTranslate(httpClient, apiUrl, text);
    }

    private String getAccessUrl(final String engine, final String sourceLang,
                                final String targetLang) {
        StringBuilder urlBuilder = new StringBuilder(API_URL);
        urlBuilder.append(engine).append("_").append(sourceLang)
                .append("_").append(targetLang).append("/");
        return urlBuilder.toString();
    }

    private String requestTranslate(final HttpClient httpClient, final String url, final String text) {
        String apiUsername = System.getProperty("textra.api.username");
        String apiKey = System.getProperty("textra.api.key");
        String apiSecret = System.getProperty("textra.api.secret");

        HttpPost httpPost = new HttpPost(url);
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(apiKey, apiSecret);
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setSocketTimeout(SO_TIMEOUT)
            .build();
        httpPost.setConfig(requestConfig);
        List<BasicNameValuePair> postParameters = new ArrayList<>(5);
        postParameters.add(new BasicNameValuePair("key", apiKey));
        postParameters.add(new BasicNameValuePair("name", apiUsername));
        postParameters.add(new BasicNameValuePair("type", "json"));
        postParameters.add(new BasicNameValuePair("text", text));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            return null;
        }

        try {
            consumer.sign(httpPost);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException
                | OAuthCommunicationException ex) {
            System.err.println(ex.getMessage());
            return null;
        }

        int respStatus;
        InputStream respBodyStream;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            respBodyStream = httpResponse.getEntity().getContent();
            respStatus = httpResponse.getStatusLine().getStatusCode();
        } catch (IOException  ex) {
            System.err.println(ex.getMessage());
            return null;
        }

        if (respStatus != 200) {
            System.err.println(String.format("Get response: %d", respStatus));
            return null;
        }

        String result;
        try (BufferedInputStream bis = new BufferedInputStream(respBodyStream)) {
            System.out.println("Http response status: " + respStatus);
            String rsp = IOUtils.toString(bis);
            JSONObject jobj = new JSONObject(rsp);
            JSONObject resultset = jobj.getJSONObject("resultset");
            result = resultset.getJSONObject("result").getString("text");
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
        return result;
    }
}
