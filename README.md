# You have successfully created an Atlassian Plugin!

### Here are the SDK commands you'll use immediately:

* **atlas-run**   -- installs this plugin into the product and starts it on localhost
  * **--product \<product name\>** starts plugin with given Atlassian product
  * **--version \<version number\>** starts given product with specified number
  * **--http-port \<port number\>** starts application at given port
  * **-DskipTests=true** to skip unit tests
  * **--jvmargs '-D\<parameter name\>=\<value\>'** to provide JVM startup parameters
* **atlas-debug** -- same as atlas-run, but allows a debugger to attach at port 5005
* **atlas-cli**   -- after atlas-run or atlas-debug, opens a Maven command line window:
  * **pi** reinstalls the plugin into the running product instance
* **atlas-help**  -- prints description for all commands in the SDK
* **atlas-create-home-zip** -- create test data archive (/target/<product>/generated-test-resources.zip) that can be referenced in POM as test fixture

### Examples: 

_atlas-debug --product jira --version 7.4.1 --http-port 2991_

_atlas-run-standalone --product confluence --jvmargs "-Datlassian.webresource.disable.minification=false -Dplugin.webresource.batching.off=true"_

Full documentation is always available at:
https://developer.atlassian.com/display/DOCS/Introduction+to+the+Atlassian+Plugin+SDK


### Miscellaneous:
``` 
netstat –aon | find "<port number>"
taskkill /F /pid <process ID>
```
