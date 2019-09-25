# Drombler JStore Client

The _Drombler JStore Client_ is a JavaFX-based rich client.

It manages multiple remote [Drombler JStore Client Agents](https://github.com/Drombler/drombler-jstore-client-agent) and allows the user to discover new applications in [Drombler JStore](https://github.com/Drombler/drombler-jstore).

For more information see the [documentation](http://www.drombler.org/drombler-jstore-client).

You can get the binary from the [latest release](https://github.com/Drombler/drombler-jstore-client/releases/latest) (early access release).

## Build the project from sources
```bash
mvn clean install
```
Please note that the develop branch (SNAPSHOT version) of the project might depend on SNAPSHOT versions of other projects.

If you don't want to build the dependent projects as well, please make sure to define a proxy in your [Maven Repository Manager](https://maven.apache.org/repository-management.html) to the following Maven Repository: https://oss.sonatype.org/content/repositories/snapshots/ and include it in your [single group](https://help.sonatype.com/repomanager3/formats/maven-repositories#MavenRepositories-ConfiguringApacheMaven).

