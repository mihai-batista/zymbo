package staruml;

public class ClassElementHelper {
	public final String ELEMENT_ID = "id";
	public final String TITLE_PARAM = "title";
	public final String X_PARAM = "x";
	public final String Y_PARAM = "y";
	public final String WIDTH_PARAM = "width";
	public final String HEIGHT_PARAM = "height";
	public final String EDITED_BY = "edited_by";
	public final String VISIBILITY = "visibility";
	public final String IS_ABSTRACT = "is_abstract";
	public final String ATTRIBUTES = "attributes";
	public final String OPERATIONS = "operations";
		
	public final int ELEMENT_ID_POS = 0;
    public final int X_POS = ELEMENT_ID_POS + 1;
    public final int Y_POS = X_POS + 1;
    public final int WIDTH_POS = Y_POS + 1;
    public final int HEIGHT_POS = WIDTH_POS + 1;
    public final int TITLE_POS = HEIGHT_POS + 1;
    public final int EDITED_BY_POS = TITLE_POS + 1;
    public final int VISIBILITY_POS = EDITED_BY_POS + 1;
    public final int IS_ABSTRACT_POS = VISIBILITY_POS + 1;
    public final int ATTRIBUTES_POS = IS_ABSTRACT_POS + 1;
    public final int OPERATIONS_POS = ATTRIBUTES_POS + 1;
    
    // Visibility values
 	public final String VISIBILITY_PUBLIC = "+";
 	public final String VISIBILITY_PRIVATE = "-";
 	public final String VISIBILITY_PROTECTED = "#";
 	public final String VISIBILITY_PACKAGE = "~";
    
 	public final String VISIBILITY_DEFAULT = "+";
 	public final String IS_ABSTRACT_DEFAULT = "0";
 	public final String ATTRIBUTES_DEFAULT = "";
 	public final String OPERATIONS_DEFAULT = "";
 	public final String EDITED_BY_DEFAULT = "none";
 	public final String DEFAULT_VALUE = "1";
 	
}
