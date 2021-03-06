// GraphLab Project: http://graphlab.sharif.edu
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/
package graphlab.plugins.commandline;

import graphlab.graph.atributeset.GraphAttrSet;
import graphlab.graph.graph.GraphModel;
import graphlab.graph.ui.GHTMLPageComponent;
import graphlab.platform.Application;
import graphlab.platform.core.BlackBoard;
import graphlab.platform.core.Listener;
import graphlab.platform.core.exception.ExceptionHandler;
import graphlab.platform.extension.ExtensionLoader;
import graphlab.platform.plugin.PluginInterface;
import graphlab.plugins.commandline.extensionloader.BSHExtensionLoader;
import graphlab.ui.ExtensionShellCommandProvider;
import graphlab.ui.UI;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author Mohammad Ali Rostami
 * @email ma.rostami@yahoo.com
 */

public class Init implements PluginInterface {
    Shell shell;
    BlackBoard bb;
//added shell's sidebar
    public void init(BlackBoard blackboard) {
        bb = blackboard;
        UI ui = blackboard.getData(UI.name);
        try {
            ui.addXML("/graphlab/plugins/commandline/config.xml", getClass());
            shell = new Shell(blackboard);

            shell.performJob("SS");
            bb.setData(Shell.NAME, shell);

            ExtensionLoader.registerUnknownExtensionLoader(new BSHExtensionLoader(shell));

            GHTMLPageComponent.registerHyperLinkHandler("BSH", new ShellHyperlinkHandler(shell));
            blackboard.addListener(Application.POST_INIT_EVENT, new Listener() {
                public void keyChanged(String key, Object value) {
                    postInit();
                }
            });

        } catch (IOException e) {
            ExceptionHandler.catchException(e);
            System.out.println("xml file was not found , or IO error");

        } catch (SAXException e) {
            ExceptionHandler.catchException(e);
        }
    }
//added commands for all extensions
    void postInit() {
        for (ExtensionShellCommandProvider c : ExtensionShellCommandProvider.commands) {
            try {
                String var = shell.newVariable();
                shell.set_variable("_" + c.trgClass.getClass().getSimpleName(), c.trgClass);
                shell.set_variable(var, c.ths);
                c.command += c.help
                        + "return " + var + "." + "performExtensionInCommandLine()" + ";"
                        + "}";
                shell.evaluateCommand(c.command, c.name, c.abrv);
                shell.addCodeCompletionDictionary(c.name, c.trgClass.getClass());
            } catch (Exception e) {
                ExceptionHandler.catchException(e);
            }
        }

        // G0 Initialization
        GraphModel gm = bb.getData(GraphAttrSet.name);
        if (gm != null)
            shell.set_variable(gm.getLabel(), gm);
        shell.ext_console.shell = shell;
    }

}
