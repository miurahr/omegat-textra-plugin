# Change Log
All notable changes to this project will be documented in this file.

## [Unreleased]

## [0.11.0] - 2020-11-12

- Add new language combinations for NT mode (#29)
- Drop NMT mode(no support from 11, Nov. 2020) (#28, #29)
- Bump gradle version up to 6.6.1
- Drop spotbugs and lessbugs check.
- make language/method combination class to static 

## [0.10.0] - 2020-10-31

### Changed
- Convert translations bundles to ascii from native

## [0.9.3] - 2020-08-02

### Fixed
- Enable MT plugin when start, past versions need to open configuration.
- Change loglevel for Debug messages.

## [0.9.2] - 2020-07-30

### Fixed
- Fix language code of Chinese (zh-CN, zh-TW) for API
  that fix machine translation against Chinese.

## [0.9.1] - 2020-05-10

### Fixed
- make Japanese UI working.
- Update CI configurations.

### Added
- Chinese and Germany translations.

## [0.9.0] - 2020-04-30

### Fixed
- Changelog commit comparison links
- Use semantic versioning.

### Added
- Add support for new translation modes.(#24)
  * General NT
  * Patent NT
  * Voicetra NMT
  * Voicetra NT
  * Finance NMT
  * Finance NT

### Changed
- Rename dialog for GeneralN to name 'General NMT Mode'
- Build against OmegaT 5.2.0
- Bamp groovy version to 3.0.1

## [0.8] - 2020-03-04

### Fixed
- Persistence of login credentials. (#21)

### Changed
- Bamp gradle version to 6.0.1.
- Build against OmegaT 4.3.0.

## [0.7] - 2019-08-09
### Fixed
- Fix configuration doesn't reflect and cause exception.(#13)

## [0.6] - 2019-05-06
### Add
- Unit test of options for Chinese languages.

### Changed
- Updated a supported language combination list
- Update build.gradle for gradle 4.8 and 5.4
- Update a referenced OmegaT version to 4.1
- Use Spotbugs instead of FindBugs for QA
- OmegatTextraMachineTranslation class now inherited from BaseTranslate

### Fixed
- Fix formatLang() internal function handle chinese not to break;

## [0.5] - 2018-11-10
### Changed
- There are general, patent and patent claim mode in option dialog
- Update Gradle version 4.8 to support OpenJDK10

### Fixed
- Catch up Web API change on TexTra.
- Fix checkstyle warnings.

## [0.4] - 2016-11-03
### Add
- Unit tests

### Changed
- Refactoring class design.
- Introduce TextraApiClient class.
- Ignore exception if fails launching web browser on desktop.

### Fixed
- Method name typo: Not found unloadPlugin method when exit.
- Build system typo for integration test.


## [0.3] - 2016-10-31
### Add
- Mode and language support validation check.

### Changed
- Use SLF4J for error message logging.

### Fixed
- Generic/JPO patent mode made exception with enum value.
- Coding style improvement.
- Potential NPE with Cache handling.


## [0.2] - 2016-10-29
### Add
- Configuration dialog for TexTra API key, secret and mode.

### Changed
- README: Remove old TexTra terms and clauses for limitations
  TexTra updates its Terms.

### Fixed
- Save configurations to OmegaT preference.


## 0.1 - 2016-09-03
### Added
- Start project.

[Unreleased]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.11.0...HEAD
[0.11.0]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.10.0...v0.11.0
[0.10.0]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.9.3...v0.10.0
[0.9.3]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.9.2...v0.9.3
[0.9.2]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.9.1...v0.9.2
[0.9.1]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.9.0...v0.9.1
[0.9.0]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.8...v0.9.0
[0.8]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.7...v0.8
[0.7]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.6...v0.7
[0.6]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.5...v0.6
[0.5]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.4...v0.5
[0.4]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.3...v0.4
[0.3]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.2...v0.3
[0.2]: https://github.com/miurahr/omegat-textra-plugin/compare/v0.1...v0.2
