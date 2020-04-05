<html>
    <body>
        <h1>Projects</h1>

        <form id="create-project" action="" onsubmit="return createProject()">
            <input type="text" name="name" placeholder="Project Name"/>
            <input type="hidden" name="jobs" value="[]"/>
            <input type="submit" value="Create" />
        </form>

        <div id="projects"></div>

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

            function createProject() {
                var project = toJSONString( document.getElementById("create-project") );
                project.jobs = [];
                var xhttp = new XMLHttpRequest();
                xhttp.open("POST", '/project', true);
                xhttp.setRequestHeader('Content-Type', 'application/json');
                xhttp.send(JSON.stringify( project ));
                return false;
            }

            function findProjects() {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                    if (xhttp.readyState === 4 && xhttp.status === 200) {
                        var projects = JSON.parse(xhttp.responseText);
                        var output = "";
                        for (var project in projects) {
                            output += "<a href='/html/project/" + projects[project].name + "'>" + projects[project].name + "</a></br>";
                        }
                        document.getElementById("projects").innerHTML = output;
                    }
                };
                xhttp.open("GET", '/project', true);
                xhttp.setRequestHeader('Content-Type', 'application/text');
                xhttp.send();
            }

            function reloadProjects() {
                findProjects();
            }

            setInterval(reloadProjects, 1500);

        </script>

    </body>
</html>
