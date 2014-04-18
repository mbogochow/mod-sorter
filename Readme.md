ModSorter
==========

This application is used to sort mods used in the ModOrganizer application.

Input
-----

ModSorter takes an xml file as input with the following format:

	<mods>
		<mod enabled="true">
	    	<name></name>
	    	<before>
				<modName></modName>
			</before>
	  	</mod>
	</mods>

Where the `name` tag should contain the name of the mod as it appears in the left pane of ModOrganizer, the `enabled` attribute is whether the mod is checked in ModOrganizer, and the `modName` entries in the `before` tag are all mod names which this mod entry should be sorted before.

Output
------

Generates a sorted modlist.txt file.  This is the file which ModOrganizer uses to keep track of the order of mods as well as which mods are enabled/disabled.  Move this file to `ModOrganizer\profiles\<profile_name>` (make a backup of your original modlist.txt file.

Note ModOrganizer will automatically place any mods which were not included in the input xml file but are installed in the `ModOrganizer\mods` directory at the end of the modlist.txt file as disabled.

To Come
=======

Generating the input XML file from the modlist.txt file from a ModOrganizer profile.