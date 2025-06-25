package com.anode.tool.graph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A generic class representing a node in a tree data structure.
 * Each node can have a parent, multiple children, content, and attributes.
 *
 * @param <T> the type of content stored in the node
 */
public class Node<T> implements Cloneable {

    private Node<T> parent; // Reference to the parent node
    private List<Node<T>> children = new LinkedList<Node<T>>(); // List of child nodes

    private T content; // Content stored in the node

    private Map<String, Object> attributes = new HashMap<String, Object>(); // Map to store node attributes

    /**
     * Constructs a node with the specified content.
     *
     * @param content the content to be stored in the node
     */
    public Node(T content) {
        this.content = content;
    }

    /**
     * Constructs a node with the specified content and parent.
     * If the parent is not null, the node is added as a child to the parent.
     *
     * @param content the content to be stored in the node
     * @param parent the parent node
     */
    public Node(T content, Node<T> parent) {
        this(content);
        this.parent = parent;

        if (parent != null)
            parent.addChild(this);
    }

    /**
     * Adds a child node to this node.
     *
     * @param child the child node to be added
     */
    public void addChild(Node<T> child) {
        children.add(child);
    }

    /**
     * Retrieves the child node at the specified index.
     *
     * @param i the index of the child node to retrieve
     * @return the child node at the specified index
     */
    public Node<T> getChild(int i) {
        return children.get(i);
    }

    /**
     * Retrieves the position of the specified child node.
     *
     * @param child the child node whose position is to be retrieved
     * @return the index of the specified child node, or -1 if the node is not found
     */
    public int getChildPosition(Node<T> child) {
        return children.indexOf(child);
    }

    /**
     * Removes and returns the child node at the specified index.
     *
     * @param i the index of the child node to remove
     * @return the removed child node
     */
    public Node<T> removeChild(int i) {
        return children.remove(i);
    }

    /**
     * Removes all child nodes from this node.
     */
    public void removeChildren() {
        children.clear();
    }

    /**
     * Retrieves the number of child nodes.
     *
     * @return the number of child nodes
     */
    public int getChildrenCount() {
        return children.size();
    }

    /**
     * Retrieves the parent node.
     *
     * @return the parent node
     */
    public Node<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent node.
     *
     * @param parent the parent node to be set
     */
    public void setParent(Node<T> parent) {
        this.parent = parent;
    }

    /**
     * Retrieves the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @return the value of the attribute, or null if the attribute does not exist
     */
    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * Sets the value of the specified attribute.
     *
     * @param attributeName the name of the attribute
     * @param attribute the value of the attribute
     * @return the previous value of the attribute, or null if the attribute did not exist
     */
    public Object setAttribute(String attributeName, Object attribute) {
        return attributes.put(attributeName, attribute);
    }

    /**
     * Checks if the specified attribute exists.
     *
     * @param attributeName the name of the attribute
     * @return true if the attribute exists, false otherwise
     */
    public boolean attributeExists(String attributeName) {
        return attributes.containsKey(attributeName);
    }

    /**
     * Checks if this node is the root node (i.e., has no parent).
     *
     * @return true if this node is the root node, false otherwise
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Checks if this node is a leaf node (i.e., has no children).
     *
     * @return true if this node is a leaf node, false otherwise
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * Retrieves the content stored in this node.
     *
     * @return the content stored in this node
     */
    public T getContent() {
        return content;
    }

    /**
     * Creates and returns a shallow copy of this node.
     * The content, parent, children, and attributes are copied.
     *
     * @return a shallow copy of this node
     */
    @Override
    public Object clone() {
        Node<T> clone = new Node<T>(content, parent);

        clone.children = new LinkedList<Node<T>>(children);

        clone.attributes = new HashMap<String, Object>(attributes);

        return clone;
    }
}
