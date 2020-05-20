package fr.usubelli.webconsole.tool.list;

import fr.usubelli.webconsole.tool.ToolDto;
import fr.usubelli.webconsole.tool.ToolRepository;

import java.util.List;

public class ListTools {
    public List<ToolDto> list() throws ListToolException {
        return new ToolRepository().getAvailableTools();
    }
}
