<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>Project ${project.name}</title>
    </head>
    <body>
        <h1>Project ${project.name}</h1>

        <form id="create-job" action="" onsubmit="return createJob('${project.name}')">
            <input id="name" type="text" name="name" placeholder="Job Name"/><br/>
            <select id="type" name="type">
                <option value="maven">Maven</option>
                <option value="node">Node</option>
            </select><br/>
            <input id="repository" type="text" name="repository" placeholder="Git repository"/><br/>
            <input id="branch" type="text" name="branch" placeholder="Git branch"/><br/>
            <input type="hidden" name="builds" value="[]"/>
            <input type="submit" value="Create" />
        </form>

        <h2>Jobs</h2>
        <div id="jobs"></div>

        <script>

            function toJSONString( form ) {
                var obj = {};
                var elements = form.querySelectorAll( "input, select, textarea" );
                for( var i = 0; i < elements.length; ++i ) {
                    var element = elements[i];
                    var name = element.name;
                    var value = element.value;

                    if( name ) {
                        obj[ name ] = value;
                    }
                }

                return obj;
            }

            function createJob(projectName) {
                var job = {
                    name: document.getElementById("name").value,
                    type: document.getElementById("type").value,
                    properties: {
                        "git.repository": document.getElementById("repository").value,
                        "git.branch": document.getElementById("branch").value
                    },
                    builds: []
                };
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", '/project/' + projectName + "/job", true);
                xhttp.setRequestHeader('Content-Type', 'application/json');
                xhttp.send(JSON.stringify( job ));
                return false;
            }

            function findProject(projectName) {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var project = JSON.parse(xhttp.responseText);
                        var output = "";
                        for (var job in project.jobs) {
                            if (project.jobs[job].builds.length > 0) {
                                output += "#" + project.jobs[job].builds[project.jobs[job].builds.length -1].number;
                                output += " ";
                                output += project.jobs[job].builds[project.jobs[job].builds.length -1].state;
                            } else {
                                output += "No build";
                            }
                            output += " <a href='/html/project/" + projectName + "/job/" + project.jobs[job].name + "'>" + project.jobs[job].name + "</a></br>";
                        }
                        document.getElementById("jobs").innerHTML = output;
                    }
                };
                xhttp.open("GET", '/project/' + projectName, true);
                xhttp.setRequestHeader('Content-Type', 'application/text');
                xhttp.send();
            }

            function reloadProject() {
                findProject('${project.name}');
            }

            setInterval(reloadProject, 1500);

        </script>

    </body>
</html>
