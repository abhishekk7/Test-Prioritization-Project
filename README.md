<h2>Steps to Run Prioritizer</h2>
<ol>
<li>Download or clone from https://github.com/abhishekk7/Test-Prioritization-Project</li>
<li>Extract the project if downloaded as zip</li>
<li>Open Eclipse(or similar IDE).</li>
<li>File > Import > Existing Maven Project and then click Next</li>
<li>Browse to the Location of Test-Prioritization-Project, Click ok</li>
<li>Choose the priorityTool pom.xml in the list and click Finish</li>
<li>Right-click on the priority tool and go to RunAs and select Maven install.</li>
<li>Get a maven project (Eg, Joda-Time, https://github.com/JodaOrg/joda-time) and set it up similarly, and make sure there are no errors.</li>
<li>Copy priorityTool-0.0.1-SNAPSHOT.jar from priorityTool/target and paste it into the project to be tested(Eg, joda-time)</li>
<li>Copy asm-5.0.3.jar from the folder into the project to be tested(Eg, joda-time).</li>
<li>Open the project to be tested's pom.xml</li>
<li>Find maven-surefire-plugin configuration and add the following lines to the plugin inside 

        <argLine>-javaagent:"${basedir}/priorityTool-0.0.1-SNAPSHOT.jar=org/joda/time"</argLine>
             <properties>
                  <property>
                       <name>listener</name>
                       <value>com.stvv.priorityTool.CommonListener</value>
                  </property>
             </properties>
  </li>
<li>Make sure that the Junit version in the pom.xml is 4.12</li>
<li>Run maven test on the project.</li>  
</ol>
