package org.knime.knip.example;

import net.imglib2.type.numeric.RealType;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.plugin.Menu;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(
        menu = { @Menu(label = "DeveloperPlugins"),
                @Menu(label = "MyBuddyDemoPlugin") },
        description = "One way to add new ImageJ plugins to KNIME is to wrap them with eclipse plugins that register themselves as buddies of"
                + " org.knime.knip.imagej.core . The automatic plugin retriev mechanism will discover the plugins, parse the annotations and add them as"
                + " KNIME nodes. However the java compiler settings of the fragment project have to be adjusted to meet the requirements of the sezpoz"
                + " library. For more details see sezpoz.java.net => Notes => Eclipse-specific notes or inspect the MyBuddyDemoPlugin."
                + "", headless = true, type = Command.class)
public class MyBuddyDemoPlugin<T extends RealType<T>> implements Command {

    @Parameter(type = ItemIO.OUTPUT)
    private String output;

    @Override
    public void run() {
        output = "plugins can be defined in registerd buddies of imagej.core";
    }

}