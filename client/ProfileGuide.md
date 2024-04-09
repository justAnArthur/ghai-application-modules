# SideBar Structure
There are 2 tabs for view and edit, each has its own gridpane, View has label pairs for attribute name and value, edit has MFXTextField for each attribute.

## View 

To add new View attribute:

add new row to gridpane if there are 0 free cells,
copy gridpane with label pairs from one of previous cells (from left to left side, from right to right)
change id of lower (value) label.
add this id to Controller with @FXML notation

## Edit 

To add new Edit attribute:

add new row to gridpane if there are 0 free cells,
copy one of previous fields, if attribute is unchangeable - set editable property to false.

