package fr.usubelli.webconsole.tool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.usubelli.webconsole.repository.DownloadException;
import fr.usubelli.webconsole.repository.ExtractException;
import fr.usubelli.webconsole.repository.InstallToolException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToolRepository {

    public List<ToolDto> getAvailableTools() {
        List<ToolDto> availableTools;
        try {
            availableTools = new ObjectMapper().readValue(new File("tools.json"), new TypeReference<List<ToolDto>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            availableTools = new ArrayList<>();
        }
        return availableTools
                .stream()
                .filter(availableTool -> availableTool.getOs().contains(getOs()))
                .collect(Collectors.toList());
    }

    public ToolDto getAvailableTool(String type, String version) {
        return getAvailableTools()
                .stream()
                .filter(availableTool -> availableTool.getType().equals(type))
                .filter(availableTool -> availableTool.getVersion().equals(version))
                .filter(availableTool -> availableTool.getOs().contains(getOs()))
                .findFirst()
                .orElse(null);
    }

    private String getOs() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "windows";
        } else if (os.contains("nix")
                || os.contains("nux")
                || os.contains("aix")) {
            return "linux";
        }
        return "unknown";
    }

    public void installTool(String type, String version) throws InstallToolException {
        final ToolDto availableTool = getAvailableTool(type, version);
        if (availableTool == null) {
            System.err.println("This tool or version is not available");
            return;
        }

        final File typedToolDirectory = new File(System.getProperty("user.home") + "/webconsole/tools",
                availableTool.getType());
        if (new File(typedToolDirectory, availableTool.getVersion()).exists()) {
            System.out.println(String.format("Tool %s already installed", availableTool.getVersion()));
            return;
        }
        try {
            String toolArchiveName = availableTool.getArchive();
            download(availableTool.getUrl(), new File(typedToolDirectory, toolArchiveName));
            extract(new File(typedToolDirectory, toolArchiveName), typedToolDirectory);
        } catch (ExtractException | DownloadException e) {
            throw new InstallToolException(e);
        }

    }

    private void download(String url, File destination) throws DownloadException {
        System.out.println(String.format("Downloading tool from %s", url));
        if (!destination.getParentFile().exists()) {
            destination.getParentFile().mkdirs();
        }
        if (destination.exists()) {
            System.out.println(String.format("File %s already exists", destination.getAbsolutePath()));
            return;
        }
        try {
            System.out.println(String.format("Start downloading %s", url));
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            fileOutputStream.close();
            in.close();
            System.out.println(String.format("Download succeded copy to %s", destination.getAbsolutePath()));
        } catch (Exception e) {
            System.err.println("Download failure");
            throw new DownloadException(e);
        }
    }

    private void extract(File archive, File destinationFolder) throws ExtractException {
        System.out.println(String.format("Extracting tool from %s", archive.getName()));
        try {
            ArchiveInputStream ais = null;
            if (archive.getName().endsWith(".tar.gz")) {
                InputStream fi = new FileInputStream(archive);
                InputStream bi = new BufferedInputStream(fi);
                InputStream gzi = new GzipCompressorInputStream(bi);
                ais = new TarArchiveInputStream(gzi);
            } else if (archive.getName().endsWith(".zip")) {
                InputStream fi = new FileInputStream(archive);
                InputStream bi = new BufferedInputStream(fi);
                ais = new ZipArchiveInputStream(bi);
            }
            ArchiveEntry entry;
            while ((entry = ais.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File curfile = new File(destinationFolder, entry.getName());
                File parent = curfile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                IOUtils.copy(ais, new FileOutputStream(curfile));
            }
            ais.close();
        } catch (IOException e) {
            throw new ExtractException(e);
        }
    }

}
