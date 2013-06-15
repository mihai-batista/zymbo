package org.umldiagram.elements;

public class AssociationElementHelper {
	public static final int ELEMENT_ID_POS = 0;
	public static final String ELEMENT_ID = "element_id";
	
	public static final int START_ELEMENT_ID_POS = ELEMENT_ID_POS + 1;
	public static final int END_ELEMENT_ID_POS = START_ELEMENT_ID_POS + 1;
	
	public static final String START_ELEMENT_ID = "start_element_id";
	public static final String END_ELEMENT_ID = "end_element_id";
	
	public static final int ELEMENT_TITLE_POS = END_ELEMENT_ID_POS + 1;
	public static final int START_NAME_POS = ELEMENT_TITLE_POS + 1;
	public static final int START_VISIBILITY_POS = START_NAME_POS + 1;
	public static final int START_IS_NAVIGABLE_POS = START_VISIBILITY_POS + 1;
	public static final int START_AGGREGATION_POS = START_IS_NAVIGABLE_POS + 1;
	public static final int START_MULTIPLICITY_POS = START_AGGREGATION_POS + 1;
	public static final int START_CONNECTED_ELEMENT_POS = START_MULTIPLICITY_POS + 1;
	
	public static final int END_NAME_POS = START_CONNECTED_ELEMENT_POS + 1;
	public static final int END_VISIBILITY_POS = END_NAME_POS + 1;
	public static final int END_IS_NAVIGABLE_POS = END_VISIBILITY_POS + 1;
	public static final int END_AGGREGATION_POS = END_IS_NAVIGABLE_POS + 1;
	public static final int END_MULTIPLICITY_POS = END_AGGREGATION_POS + 1;
	public static final int END_CONNECTED_ELEMENT_POS = END_MULTIPLICITY_POS + 1;
	
	public static final int GENERALIZATION_POS = END_CONNECTED_ELEMENT_POS + 1;
	public static final int REALIZATION_POS = GENERALIZATION_POS + 1;
	public static final int DEPENDENCY_POS = REALIZATION_POS + 1;
	
	public static final String ELEMENT_TITLE = "element_title";
	public static final String START_NAME = "start_name";
	public static final String START_VISIBILITY = "start_visibility";
	public static final String START_IS_NAVIGABLE = "start_is_navigable";
	public static final String START_AGGREGATION = "start_aggregation";
	public static final String START_MULTIPLICITY = "start_multiplicity";
	public static final String START_CONNECTED_ELEMENT = "start_connected_element";
	
	public static final String END_NAME = "end_name";
	public static final String END_VISIBILITY = "end_visibility";
	public static final String END_IS_NAVIGABLE = "end_is_navigable";
	public static final String END_AGGREGATION = "end_aggregation";
	public static final String END_MULTIPLICITY = "end_multiplicity";
	public static final String END_CONNECTED_ELEMENT = "end_connected_element";
	
	public static final String GENERALIZATION = "generalization";
	public static final String REALIZATION = "realization";
	public static final String DEPENDENCY = "dependency";
	
	// DEFAULT VALUES
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_VISIBILITY = "PUBLIC";
	public static final String DEFAULT_IS_NAVIGABLE = "0";
	public static final String DEFAULT_AGGREGATION = "NONE";
	public static final String DEFAULT_MULTIPLICITY = "";
	public static final String DEFAULT_CONNECTED_ELEMENT = "none";
	
	// Visibility values
	public static final String VISIBILITY_PUBLIC = "+";
	public static final String VISIBILITY_PRIVATE = "-";
	public static final String VISIBILITY_PROTECTED = "#";
	public static final String VISIBILITY_PACKAGE = "~";
	
	// Multiplicity values
	public static final String MULTIPLICITY_NONE = "";
	public static final String MULTIPLICITY_0_1 = "0..1";
	public static final String MULTIPLICITY_1 = "1";
	public static final String MULTIPLICITY_0_n = "0..*";
	public static final String MULTIPLICITY_1_n = "1..*";
	public static final String MULTIPLICITY_n = "*";
	
	public static final String AGGREGATION_NONE = "NONE";
	public static final String AGGREGATION_AGGREGATE = "AGGREGATE";
	public static final String AGGREGATION_COMPOSITE = "COMPOSITE";
	
}
