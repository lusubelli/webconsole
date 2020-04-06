package fr.usubelli.webconsole;

import fr.usubelli.webconsole.dto.BuildDto;
import fr.usubelli.webconsole.dto.JobDto;
import fr.usubelli.webconsole.dto.ProjectDto;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PageBuilder {

    private final Configuration cfg;

    PageBuilder(File templateFolder) throws IOException {
        this.cfg = new Configuration(freemarker.template.Configuration.VERSION_2_3_29);
        this.cfg.setDirectoryForTemplateLoading(templateFolder);
        this.cfg.setDefaultEncoding("UTF-8");
        this.cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        this.cfg.setLogTemplateExceptions(false);
        this.cfg.setWrapUncheckedExceptions(true);
        this.cfg.setFallbackOnNullLoopVariable(false);
    }

    String buildWelcomePage(List<ProjectDto> projects) {
        Map<String, Object> root = new HashMap<>();
        root.put("projects", projects);
        return process(root, "welcome.ftlh");

    }

    String buildProjectPage(ProjectDto project) {
        Map<String, Object> root = new HashMap<>();
        root.put("project", project);
        return process(root, "project.ftlh");

    }

    String buildJobPage(ProjectDto project, JobDto job) {
        Map<String, Object> root = new HashMap<>();
        root.put("project", project);
        root.put("job", job);
        return process(root, "job.ftlh");
    }

    String buildBuildPage(ProjectDto project, JobDto job, BuildDto build) {
        Map<String, Object> root = new HashMap<>();
        root.put("project", project);
        root.put("job", job);
        root.put("build", build);
        return process(root, "build.ftlh");
    }

    private String process(Map<String, Object> root, String template) {
        Template temp = null;
        try {
            temp = cfg.getTemplate(template);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Writer out = new StringWriter();
        try {
            temp.process(root, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toString();
    }

}
