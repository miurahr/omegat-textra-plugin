# NICT TexTra Machine Translation API plugin for OmegaT

This is an implementation of OmegaT plugin which support NICT TexTra Machine Translation API.

## Install

Please download zip file from Github release. You can get jar file from zip distribution.
OmegaT plugin should be placed in `$HOME/.omegat/plugin` or `C:\Program Files\OmegaT\plugin`
depending on your operating system.

## Configuration

You can configure the plugin using **Options > Machine Translate > TexTra > Options**.
After setting configurations, you may enable plugin at **Options > Machine Translate > TexTra > Enable**
When enabled, machine translations suggestions will apear in the Machine Translation pane automatically.

## TexTra Terms

You need to agree NICT TexTra Service terms  and  get an account (username, api key and api secret)
to use this plugin with OmegaT. The terms show at
https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/policy/

## TexTra TLS certification

NICT TexTra uses Starfield G2 certificate for their https communication.
Some java version does not includes its root certificate as tructed one.
You may need to import its certification as trusted one from Java application.

To download certification, please go to;
`https://certs.secureserver.net/repository/`
and download `sfroot-g2.crt`

then import a cert, for example on Ubuntu/Mint;

```
sudo keytool -importcert -trustcacerts -file sfroot-g2.crt -keystore /usr/lib/jvm/java-8-openjdk-amd64/jre/lib/security/cacerts -alias starfield-g2 -storepass changeit
```

please check carefully with sha256 footprint on the site and keytool's notification.

## License

This project is distributed under the GNU general public license version 3 or later.

