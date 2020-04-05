<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>Build #${build.number}</title>
    </head>
    <body>
        <h1>Build #${build.number}</h1>
        <div id="state"></div><br/>
        <textarea id="console" cols="200" rows="20"></textarea>

        <script type="text/javascript">

            function readLogs(projectName, jobName, buildNumber) {
                var xhttp = new XMLHttpRequest();
                xhttp.onprogress  = function() {
                            var textarea = document.getElementById("console");
                            textarea.value += xhttp.responseText;
                            textarea.scrollTop = textarea.scrollHeight;
                };
                xhttp.open("GET", '/project/' + projectName + '/job/' + jobName + '/build/' + buildNumber + "/logs", true);
                xhttp.setRequestHeader('Content-Type', 'application/text');
                xhttp.send();
            }

            function findBuild(projectName, jobName, buildNumber) {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var build = JSON.parse(xhttp.responseText);
                        document.getElementById("state").innerHTML = build.state;
                    }
                };
                xhttp.open("GET", '/project/' + projectName + '/job/' + jobName + "/build/" + buildNumber, true);
                xhttp.setRequestHeader('Content-Type', 'application/text');
                xhttp.send();
            }

            function reloadBuild() {
                findBuild('${project.name}', '${job.name}', '${build.number}');
            }

            setInterval(reloadBuild, 800);

            readLogs('${project.name}', '${job.name}', ${build.number});

        </script>

    </body>
</html>
