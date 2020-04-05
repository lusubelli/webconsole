<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>Job ${job.name}</title>
    </head>
    <body>
        <h1>Job ${job.name}</h1>

        <div>
            <h3>--- Properties ---</h3>
            <#list job.properties as name, value>
                <p>${name}: ${value}</p>
            </#list>
        </div>
        </br>

        <button onclick="runBuild('${project.name}', '${job.name}')">Run</button>

        <h2>Builds</h2>
        <div id="builds"></div>

        <script type="text/javascript">

            function runBuild(projectName, jobName) {
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", '/project/' + projectName + '/job/' + jobName + '/run', true);
                xhttp.setRequestHeader('Content-Type', 'application/text');
                xhttp.send();
            }

            function findJob(projectName, jobName) {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var job = JSON.parse(xhttp.responseText);
                        var output = "";
                        for (var build in job.builds) {
                            output += "<a href='/html/project/" + projectName + "/job/" + jobName + "/build/" + job.builds[build].number + "'>#" + job.builds[build].number + "</a>" + job.builds[build].state + "</br>";
                        }
                        document.getElementById("builds").innerHTML = output;
                    }
                };
                xhttp.open("GET", '/project/' + projectName + '/job/' + jobName, true);
                xhttp.setRequestHeader('Content-Type', 'application/text');
                xhttp.send();
            }

            function reloadJob() {
                findJob('${project.name}', '${job.name}');
            }

            setInterval(reloadJob, 800);

        </script>

    </body>
</html>
