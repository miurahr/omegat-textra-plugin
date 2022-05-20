# NICT TexTra Machine Translation API plugin for OmegaT

This is an implementation of OmegaT plugin which support NICT TexTra Machine Translation API which are
provided by NiCT for non-profit and OSS translations and Kawamura-International for Business translations.

* [日本語の利用方法の説明](https://github.com/miurahr/omegat-textra-plugin/wiki/%E5%88%A9%E7%94%A8%E6%96%B9%E6%B3%95)

* [Usage manual in English](https://github.com/miurahr/omegat-textra-plugin/wiki/Usage)


## NEWS

- **10, Apr. 2022** - **Version 2022.1.0** - Update dependency libraries: Jackson 2.13.2, commons-io 2.11.0 and http-client 4.5.13
- **16, Jan. 2021** - **Version 2020.2.1** - Support adaptive translation engine(custom id: a-*).
- **15, Nov. 2020** - **Version 2020.2.0** - Support custom engine configuration(custom id: c-*).
- **14, Nov. 2020** - **Version 2020.1.0** - Support business account and change version schema with **YEAR**.

## Install

Please download latest omegat-textra-plugin-x.x.zip file from [releases](https://github.com/miurahr/omegat-textra-plugin/releases) page 
in this Github repository. You can get a plugin file (omegat-textra-plugin-x.x.jar) from downloaded zip distribution.
The OmegaT plugin should be placed in `$HOME/.omegat/plugins` or `C:\Program Files\OmegaT\plugins`
depending on your operating systemc.

## Requirements

- Java 11 Runtime 

Note: Java 8 Runtime requiires modification of bundled TLS root certifications to run the plugin. See manual.

## Configuration

You can enable the plugin using **Options > Preferences... > Machine Translation** to check `Textra by NICT` on.
After enables configurations, it is necessary to configure TexTra username, API key and secret
on a dialog shown when pushing **Configure** button

The information can be obtained from a link shown on the dialog. 
After configured, suggestions will appear in the Machine Translation pane automatically.

### Windows

On Windows you can install the plugin to the plugins directory where OmegaT is installed
 (e.g. C:\Program Files\OmegaT) or to your Application Data directory:

Windows 10: C:\Users<username>\AppData\Roaming\OmegaT

### Mac OS X

On OS X you are recommended to install the plugin to /Users//Library/Preferences/OmegaT/plugins.
 The Library folder in your home directory may be hidden ; to access it from the Finder,
select Go > Go to Folder from the main menu and enter ~/Library/Preferences/OmegaT/plugins.

### Linux & BSD

On Linux and BSD you can install the plugin to the plugins directory where OmegaT is
installed (alongside OmegaT.jar) or to ~/.omegat/plugins.

## TexTra Terms and API key

You need to agree NICT TexTra Service terms  and  get an account (username, api key and api secret)
to use this plugin with OmegaT. The terms show at
https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/policy/

## TexTra TLS certification

NICT TexTra uses Starfield G2 certificate for their https communication.
Java8 does not includes its root certificate as trusted one.
You may need to import its certification as trusted one from Java application.

- Java 11.0.3 (Apr. 16, 2019) include the certification.
https://www.oracle.com/technetwork/java/javase/11-0-3-oracle-relnotes-5290048.html

- Java bug tracking
https://bugs.java.com/bugdatabase/view_bug.do?bug_id=8207191


To download certification, please go to;
`https://certs.secureserver.net/repository/`
and download `sfroot-g2.crt`

then import a cert, for example on  Mac:

```
sudo keytool -importcert -trustcacerts -file sfroot-g2.crt -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts -alias starfield-g2 -storepass changeit
```

Please check it carefully with sha256 footprint on the site and keytool's notification.

On Ubuntu/Mint, please check your certs directory where exists
starfield G2 certificate as /etc/ssl/certs/Starfield_Root_Certificate_Authority_-_G2.crt
then manually run

```
sudo update-ca-certificates
```

## Supported language combinations

### NICT non-profit and OSS translations account

* languages
    * English
    * Japanese
    * Chinese(Mandarin, Taiwanese)
    * Italian
    * Indonesian
    * French
    * Portuguese
    * Myanmar
    * Thai
    * Vietnamese
    * Spanish
    * Arabic
    * Russian
    * Germany
    * Korean
    * Philippian

* Engines
    * General NT
    * Finance NT
    * Patent NT
    * General NT+ (minnaNT)
    * VoiceTra NT
    
    * CUSTOM engine

### Kawamura-Internaltional Personal Business account

* Languages
    * Japanese
    * Chinese(Mandarin, Taiwan)
    * English
* Engines
    * General NT
    * VoiceTra NT
    * Patent NT
    * Science
    * Finance

## License

The plugin is distributed under the GNU general public license version 3 or later.

