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
Some java installation does not includes its root certificate as tructed one.
You may need to import its certification as trusted one from Java application.

To download certification, please go to;
`https://certs.secureserver.net/repository/`
and download `sfroot-g2.crt`

then import a cert, for example on  Mac:

```
sudo keytool -importcert -trustcacerts -file sfroot-g2.crt -keystore /Library/Java/JavaVirtualMachines/jdk1.8.0_121.jdk/Contents/Home/jre/lib/security/cacerts -alias starfield-g2 -storepass changeit
```

please check carefully with sha256 footprint on the site and keytool's notification.

On Ubuntu/Mint, please check your certs directory where exists
starfield G2 certificate as /etc/ssl/certs/Starfield_Root_Certificate_Authority_-_G2.crt
then manually run

```
sudo update-ca-certificates
```


## License

This project is distributed under the GNU general public license version 3 or later.

