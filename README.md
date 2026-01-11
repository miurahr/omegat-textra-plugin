# NICT TexTra Machine Translation API plugin for OmegaT

This is an implementation of OmegaT plugin which supports NICT TexTra Machine Translation API which is 
provided by NiCT for non-profit and OSS translations, and Kawamura-International for Business translations.

* [日本語の利用方法の説明](https://github.com/miurahr/omegat-textra-plugin/wiki/%E5%88%A9%E7%94%A8%E6%96%B9%E6%B3%95)

* [Usage manual in English](https://github.com/miurahr/omegat-textra-plugin/wiki/Usage)

When you are looking for Mirai Translator(R) connector, based on NICT engine and NTT collaboration,
you may be interested in [MT plugin for OmegaT](https://codeberg.org/miurahr/omegat-mirai).

## NEWS

- **17, Jul. 2023** - **Version 2023.3.0** - Support OmegaT 6.0 and later, and drop support 5.8.0 and before.
- **23, Apr. 2023** - **Version 2023.2.0** - Support OmegaT 5.8 and later, and drop support 5.7.1 and before.
- **04, Feb. 2023** - **Version 2023.1.0** - Update http access code, Jackson 2.13.4. Last version that supports OmegaT 4.3.3.

## Install

Please download latest omegat-textra-plugin-x.x.zip file from [releases](https://github.com/miurahr/omegat-textra-plugin/releases) page 
in this repository. You can get a plugin file (omegat-textra-plugin-x.x.jar) from downloaded zip distribution.
The OmegaT plugin should be placed in `$HOME/.omegat/plugins` or `C:\Users\username\AppData\Roaming\OmegaT\plugins` depending on your operating systemc.

## Requirements

- OmegaT 6.0.0 or later
- Java runtime version 11.0.3 or later

## Configuration

You can enable the plugin using **Options > Preferences... > Machine Translation** to check `Textra by NICT` on.
After enables configurations, it is necessary to configure TexTra username, API key and secret
on a dialog shown when pushing **Configure** button

The information can be obtained from a link shown in the dialog. 
After configured, suggestions will appear in the Machine Translation pane automatically.

### Windows

On Windows you can install the plugin to the plugins directory in your Application Data directory:

Windows 10: C:\Users<username>\AppData\Roaming\OmegaT

### Mac OS X

On OS X you are recommended to install the plugin to /Users//Library/Preferences/OmegaT/plugins.
The Library folder in your home directory may be hidden; to access it from the Finder,
select Go > Go to Folder from the main menu and enter ~/Library/Preferences/OmegaT/plugins.

### Linux & BSD

On Linux and BSD you can install the plugin to the plugins directory where OmegaT is
installed (alongside OmegaT.jar) or to ~/.omegat/plugins.

## TexTra Terms and API key

You need to agree NICT TexTra Service terms and get an account (username, api key and api secret)
to use this plugin with OmegaT. The terms show at
https://mt-auto-minhon-mlt.ucri.jgn-x.jp/content/policy/

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
