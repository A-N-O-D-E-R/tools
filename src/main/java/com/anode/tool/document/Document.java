package com.anode.tool.document;


import java.math.BigDecimal;
import java.util.List;

/*
 * @author Deepak Arora
 *
 * All methods in this file that take in a path as a parameter throw an exception in case the path is incorrectly formed
 */
public interface Document {

  /**
   * Returns a boolean specifying if this document is a typed document
   */
  boolean isTyped();

  /**
   * Returns the type of the document
   */
  String getType();

  /**
   * Returns the data type of the leaf node
   */
  DataType getLeafNodeDataType(String path, String... vargs);

  /**
   * Returns the date type of the leaf node
   */
  DataType getArrayValueLeafNodeDataType(String path, String... vargs);

  /**
   * Sets the type of a document. The model object needs to be already loaded. Validation against the model will
   * be carried out an an exception thrown if a violation is found
   */
  void setType(String type);

  /**
   * Sets the type of a document. The model object needs to be already loaded. Validation against the model will
   * be carried out as per the value of the variable validateAtReadWriteOnly and an exception thrown if a violation is found
   * <p>
   * This method is deprecated - use the new method setType(String type, CONSTS_JDOCS.VALIDATION_TYPE validationType)
   */
  @Deprecated
  void setType(String type, boolean validateAtReadWriteOnly);

  /**
   * Sets the type of a document. The model object needs to be already loaded. Validation against the model will
   * be carried out as per the value of the variable validationType and an exception thrown if a violation is found
   */
  void setType(String type, validationTypes validationType);

  /**
   * Empty the contents of the document
   */
  void empty();

  /**
   * Delete the specified list of paths from the document
   * <p>
   * Throws an exception if the path is not found in the associated model document if document is typed or
   * if the name in name value pair is not of the right type
   *
   * @param pathsToDelete the list of paths
   */
  void deletePaths(List<String> pathsToDelete);

  /**
   * Delete the specified path from the document. Does nothing if the path is not found. The path specified
   * can be any path including pointing to a leaf, complex or an array node
   * <p>
   * Throws an exception if the path is not found in the associated model document
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   */
  void deletePath(String path, String... vargs);

  /**
   * Gets the size of the array at the path specified. The path specified has to be of array type in the json.
   * <p>
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return the size of the array. If the path is not found in the document, a value of 0 is returned
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the path specified does not contain empty square brackets in the end i.e. []
   *                        If the node in the json document corresponding to the path is not of an array type
   */
  int getArraySize(String path, String... vargs);

  /**
   * Gets the index of an element in the array that contains a field equal to the value as specified in the filter
   * criteria
   * <p>
   * Throws an exception
   * If the document is a typed document and the path is not found in the associated model document
   * If the path specified does not end with a filter criteria definition i.e. [field=value]
   * If the node in the json document corresponding to the path is not of an array type
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return the index of the matched element otherwise -1. The index of the first matched element will be returned
   */
  int getArrayIndex(String path, String... vargs);

  /**
   * Gets the value stored in the given path as a Boolean
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  Boolean getBoolean(String path, String... vargs);

  /**
   * Gets the value stored in the given path as an Integer
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  Integer getInteger(String path, String... vargs);

  /**
   * Gets the value stored in the given path as a String
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  String getString(String path, String... vargs);

  /**
   * Gets the value stored in the given path as a Long
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  Long getLong(String path, String... vargs);

  /**
   * Gets the value stored in the given path as a BigDecimal
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the BigDecimal value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  BigDecimal getBigDecimal(String path, String... vargs);

  /**
   * Gets the value stored in the given path as an Object
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the value stored at the path as an object of the type Integer / Long / BigDecimal / String / Boolean else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  Object getValue(String path, String... vargs);

  /**
   * Gets the value stored in the given array vaalue path as an Object
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the value stored at the path as an object of the type Integer / Long / BigDecimal / String / Boolean else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  Object getArrayValue(String path, String... vargs);

  /**
   * Gets the value stored in the array element in the given path as a Boolean
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified does not point to a leaf node
   */
  Boolean getArrayValueBoolean(String path, String... vargs);

  /**
   * Gets the value stored in the array element in the given path as an Integer
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  Integer getArrayValueInteger(String path, String... vargs);

  /**
   * Gets the value stored in the array element in the given path as a String
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  String getArrayValueString(String path, String... vargs);

  /**
   * Gets the value stored in the array element in the given path as a Long
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  Long getArrayValueLong(String path, String... vargs);

  /**
   * Gets the value stored in the array element in the given path as a BigDecimal
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return For a base document, the Boolean value stored at the path else null
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  BigDecimal getArrayValueBigDecimal(String path, String... vargs);

  /**
   * Get the JSON string for the document in a compressed format
   *
   * @return
   */
  String getJson();

  /**
   * Get the JSON string for the document in a pretty format
   *
   * @return
   */
  String getPrettyPrintJson();

  /**
   * Used to determine if the specified path exists in the document
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return true if the path exists else false
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   */
  boolean pathExists(String path, String... vargs);

  /**
   * Used to determine if the specified path is an array in the document
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return true if the path is an array else false
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   */
  boolean isArray(String path, String... vargs);

  /**
   * Get a document from a non leaf node
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return Document
   * @throws UnifyException If path is not found or path is a leaf node
   */
  Document getDocument(String path, String... vargs);

  /**
   * Returns the content of the specified path as a new document
   * The path can only point to a complex object or an array element. It can also point to an array only if
   * the includeFullkPath variable is true
   *
   * @param path                the path in the document from which the contents are to be extracted
   * @param returnTypedDocument only applicable if this document is a JDocument. If true, an instance of JDocument
   *                            is returned else an instance of BaseDocument
   * @param includeFullPath     the returned document is constructed with the full path from root else the path from root is skipped
   * @param vargs               the values to replace the % characters in path
   * @throws UnifyException If the path specified does not point to a complex object
   *                        If the return document is a JDocument but does not conform to the model document
   *                        If path points to an array but the includeFullPath is false
   */
  Document getContent(String path, boolean returnTypedDocument, boolean includeFullPath, String... vargs);

  /**
   * Copies content from document to this document. This functionality will overwrite all content
   * under the to path with the contents of the object in the from path
   *
   * @param fromDoc  the document to copy from
   * @param fromPath the path in the document to copy from (may contain % placeholders)
   * @param toPath   the path in this document to copy to (may contain % placeholders)
   * @param vargs    the values to replace the % characters in from path and to path
   * @throws UnifyException If the from document is a typed document and the path is not found in the associated model document
   *                        If the types of the from and to documents do not match
   *                        If either of the paths point to a leaf node
   *                        If the to document is a typed document and if any of the resulting paths is not present in the model document
   */
  void setContent(Document fromDoc, String fromPath, String toPath, String... vargs);

  /**
   * Set the specified value in the specified path
   *
   * @param path  the path
   * @param value the value to set in the path
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the path specified does not point to a leaf node
   */
  void setBoolean(String path, boolean value, String... vargs);

  /**
   * Set the specified value in the specified path
   *
   * @param path  the path
   * @param value the value to set in the path
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the path specified does not point to a leaf node
   */
  void setInteger(String path, int value, String... vargs);

  /**
   * Set the specified value in the specified path
   *
   * @param path  the path
   * @param value the value to set in the path
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the path specified does not point to a leaf node
   */
  void setLong(String path, long value, String... vargs);

  /**
   * Set the specified value in the specified path
   *
   * @param path  the path
   * @param value the value to set in the path
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the path specified does not point to a leaf node
   */
  void setBigDecimal(String path, BigDecimal value, String... vargs);

  /**
   * Set the specified value in the specified path
   *
   * @param path  the path
   * @param value the value to set in the path
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the path specified does not point to a leaf node
   */
  void setString(String path, String value, String... vargs);

  /**
   * Sets the specified value in the specified array element in the given path
   *
   * @param path  the path
   * @param value the value to set
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  void setArrayValueBoolean(String path, boolean value, String... vargs);

  /**
   * Sets the specified value in the specified array element in the given path
   *
   * @param path  the path
   * @param value the value to set
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  void setArrayValueInteger(String path, int value, String... vargs);

  /**
   * Sets the specified value in the specified array element in the given path
   *
   * @param path  the path
   * @param value the value to set
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  void setArrayValueLong(String path, long value, String... vargs);

  /**
   * Sets the specified value in the specified array element in the given path
   *
   * @param path  the path
   * @param value the value to set
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  void setArrayValueBigDecimal(String path, BigDecimal value, String... vargs);

  /**
   * Sets the specified value in the specified array element in the given path
   *
   * @param path  the path
   * @param value the value to set
   * @param vargs the values to replace the % characters in path
   * @throws UnifyException If the document is a typed document and the path is not found in the associated model document
   *                        If the document is a typed document and the type of the path in the model document does not match
   *                        If the type of the value specified in the path in the document does not match
   *                        If the path specified is not of array element type
   */
  void setArrayValueString(String path, String value, String... vargs);

  /**
   * Create a copy of this document
   *
   * @return the new document
   */
  Document deepCopy();

  /**
   * Merge the contents of one document into another. Both the documents need to be typed documents and of the same type.
   * The key field of each array element in the document to be merged must be specified. The deletion of specified paths
   * is carried out prior to the merge of the document fragment
   *
   * @param d             the document fragment that needs to be merged into this document
   * @param pathsToDelete the list of paths to delete from this document before merge
   */
  void merge(Document d, List<String> pathsToDelete);

  /**
   * Get the list of paths existing in the document
   */
  List<String> flatten();

  /**
   * Get the list of paths existing in the document along with the value in each path as a string
   */
  List<PathValue> flattenWithValues();

  /**
   * Compares two documents and return the results in a list. The document on which the method is
   * invoked is assumed to be the left document
   *
   * @param right           the right document to compare
   * @param onlyDifferences specifies if only difference results are to be returned or all
   */
  List<DiffInfo> getDifferences(Document right, boolean onlyDifferences);

  /**
   * Compares two subdocuments and return the results in a list. The document on which the method is
   * invoked is assumed to be the left document
   *
   * @param leftPath        the subdocument inside of the specified path
   * @param right           the right document to compare
   * @param rightPath       the subdocument inside of the specified path
   * @param onlyDifferences specifies if only difference results are to be returned or all
   */
  public List<DiffInfo> getDifferences(String leftPath, Document right, String rightPath, boolean onlyDifferences);

  /**
   * validates a document as per a model and throws an exception if not met
   * All validation errors are collected and returned in the exception
   *
   * @param type the type of the document
   * @throws UnifyException exception containing the list of errors encountered
   *
   * This method is deprecated - use the new methods validateAllPaths or validateModelPaths
   */
  @Deprecated
  public void validate(String type);

  /**
   * validates all data paths in the document against a model and throws an exception if not met
   * All validation errors are collected and returned in the exception
   *
   * @param type the type of the document
   * @throws UnifyException exception containing the list of errors encountered
   */
  public void validateAllPaths(String type);

  /**
   * validates only those data paths in the document that are found in the model and throws an exception if not met
   * All validation errors are collected and returned in the exception
   *
   * @param type the type of the document
   * @throws UnifyException exception containing the list of errors encountered
   */
  public void validateModelPaths(String type);

  /**
   * checks if the path passed is a leaf node or not
   *
   * @param path  the path
   * @param vargs the values to replace the % characters in path
   * @return true if the path is a leaf node in the document
   * @throws UnifyException if the path is not found in the document
   */
  public boolean isLeafNode(String path, String... vargs);

  /**
   * returns the validation type in effect
   */
  public validationTypes getValidationType();

}
