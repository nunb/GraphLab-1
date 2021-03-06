// GraphLab Project: http://graphlab.sharif.edu
// Copyright (C) 2008 Mathematical Science Department of Sharif University of Technology
// Distributed under the terms of the GNU General Public License (GPL): http://www.gnu.org/licenses/

package graphlab.platform.extension;

import graphlab.platform.core.BlackBoard;

import java.io.File;

/**
 * An ExtensionHandler which handles not .class files as extension. for exaple .m Matlab files and ...
 *
 * @author Azin Azadi
 */
public interface UnknownExtensionLoader {

    public Extension load(File file, BlackBoard blackboard);

}