TO DO
====

# TO BE DELETED ONCE PROJECT GOES LIVE

Check encapsulation as tight as possible.

Write messages in GuiMessageController to cater for a change of turn or the current turn being ongoing

Explore a better approach that system.exit(1) to shutting down when a player closes their GUI.

Put in place a check to see if there is a mandatory move as part of the isLegal checks, and reject the move if it isn't
an overtaking move.

Check that synchronisation is as tight as it could be. Consider making some of the classes with static methods 
into objects.

Testing

Explore whether setEnabled is being used correctly when draw offers are made and when they expire. 

Can I get rid of amIRed and have a Colour object instead, for simplicity?

Can we get rid of dictionaries by storing the clientIds in objects? 

Ensure README up-to-date and complete. Consider JavaDocs. Put notes about the GUI in the readme.

