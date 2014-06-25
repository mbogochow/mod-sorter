ModSorter
==========

This application is used to sort mods used in the [Mod Organizer](http://www.nexusmods.com/skyrim/mods/1334) application.

Input
-----

ModSorter takes an XML file as input with the following format:

	<mods>
		<mod enabled="true" external="false">
	    	<name></name>
	    	<before>
				<modName></modName>
			</before>
	  	</mod>
	</mods>

Where the `name` tag should contain the name of the mod as it appears in the left pane of ModOrganizer, the `enabled` attribute is whether the mod is checked in ModOrganizer, the `external` attribute is whether the mod was installed external to Mod Organizer, and the `modName` entries in the `before` tag are all mod names which this mod entry should be sorted before.

Output
------

Generates a sorted `modlist.txt` file.  This is the file which Mod Organizer uses to keep track of the order of mods as well as which mods are enabled/disabled.  Move this file to `ModOrganizer\profiles\<profile_name>`.

Note Mod Organizer will automatically place any mods which were not included in the input XML file but are installed in the `ModOrganizer\mods` directory at the end of the modlist.txt file as disabled.  Also, any mods which are included in the `modlist.txt` file but are not in the Mod Organizer mods XML will be ignored by Mod Organizer but left in the file.

Conversely, a sorter XML file can be generated from an existing `modlist.txt`.  In addition, an existing sorter XML file's mods can be updated with information from an existing `modlist.txt`.  The information which can be updated is additional mods and the `enabled` and `external` attributes.

To Come
=======

Better GUI with better selection of the location of the Mod Organizer installation and auto loading of available profiles.  Also better output system.