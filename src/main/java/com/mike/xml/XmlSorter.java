package com.mike.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlSorter {
	private static final Logger LOG = LoggerFactory.getLogger(XmlSorter.class);
	
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			sort("C:\\Users\\myarbrou\\Desktop\\properties.xml", "C:\\Users\\myarbrou\\Desktop\\req2.xml");
//			analyze("C:\\Users\\myarbrou\\Desktop\\req.xml");
			LOG.info("Finished in " + (System.currentTimeMillis() - start)/1000.0 + " seconds");
		} catch (Exception e) {
			LOG.error("", e);
		}
	}
	
	public static void analyze(String filepath) throws Exception {
		Document fileDocument = readFile(filepath);
	    System.out.println(fileDocument.getParentNode());
	    analyzeNode(fileDocument, 0);
	}
	
	public static void sort(String filepath, String sortedFilepath) throws Exception {
		Document fileDocument = readFile(filepath);
	    
	    sortChildren(fileDocument);
		
	    LOG.debug("Writing to file {}", sortedFilepath);
		// write the new filexml back to the file
		DOMSource source = new DOMSource(fileDocument);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result = new StreamResult(new File(sortedFilepath));
		transformer.transform(source, result);
	}

	private static Document readFile(String filepath) throws ParserConfigurationException, SAXException, IOException {
		LOG.debug("Reading from file {}", filepath);
		File xmlFile = new File(filepath);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
	    Document fileDocument = documentBuilder.parse(xmlFile);
		return fileDocument;
	}
	
	public static int compareNodes(Node node1, Node node2) {
		LOG.debug("Sorting children 1...");
		sortChildren(node1);
		LOG.debug("Sorting children 2...");
		sortChildren(node2);
		
		LOG.trace("Does the node type {} equal {}?", node1.getNodeType(), node2.getNodeType());
		if(node1.getNodeType() != node2.getNodeType()) {
			LOG.debug("The node type {} does not equal {}, returning {}", node1.getNodeType(), node2.getNodeType(), Short.compare(node1.getNodeType(), node2.getNodeType()));
			return Short.compare(node1.getNodeType(), node2.getNodeType());
		}
		
		LOG.trace("Does the node name {} equal {}?", node1.getNodeName(), node2.getNodeName());
		if(!node1.getNodeName().equals(node2.getNodeName())) {
			LOG.debug("The node name {} does not equal {}, returning {}", node1.getNodeName(), node2.getNodeName(), compare(node1.getNodeName(), node2.getNodeName()));
			return compare(node1.getNodeName(), node2.getNodeName());
		}

		NodeList children1 = node1.getChildNodes();
		NodeList children2 = node2.getChildNodes();

		LOG.trace("Does the amount of child nodes {} equal {}?", children1.getLength(), children2.getLength());
		if(children1.getLength() != children2.getLength()){
			LOG.debug("The amount of child nodes {} does not equal {}, returning {}", children1.getLength(), children2.getLength(), Integer.compare(children1.getLength(), children2.getLength()));
			return Integer.compare(children1.getLength(), children2.getLength());
		}

		LOG.trace("Do the nodes even have children?");
		if(children1.getLength() == 0) {
			LOG.debug("The nodes do not have children. Comparing values {} and {} and returning {}", node1.getNodeValue(), node2.getNodeValue(), compare(node1.getNodeValue(), node2.getNodeValue()));
			return compare(node1.getNodeValue(), node2.getNodeValue());
		}

		int comp = 0;
		for(int i = 0; i < children1.getLength(); i++) {
			LOG.trace("Comparing nodes at position {}", i);
			comp = compareNodes(children1.item(i), children2.item(i));
			if(comp!=0) {
				LOG.debug("Nodes at position {} were different. Returning {}", i, comp);
				return comp;
			}
		}

		LOG.debug("Thes nodes are the same. returning 0");
		return 0;
	}
	
	private static int compareNodes(SortedNode snode1, SortedNode snode2) {
		Node node1 = snode1.node;
		Node node2 = snode2.node;

		LOG.debug("Sorting children 1...");
		snode1.sort();
		LOG.debug("Sorting children 2...");
		snode2.sort();
		
		LOG.trace("Does the node type {} equal {}?", node1.getNodeType(), node2.getNodeType());
		if(node1.getNodeType() != node2.getNodeType()) {
			LOG.debug("The node type {} does not equal {}, returning {}", node1.getNodeType(), node2.getNodeType(), Short.compare(node1.getNodeType(), node2.getNodeType()));
			return Short.compare(node1.getNodeType(), node2.getNodeType());
		}
		
		LOG.trace("Does the node name {} equal {}?", node1.getNodeName(), node2.getNodeName());
		if(!node1.getNodeName().equals(node2.getNodeName())) {
			LOG.debug("The node name {} does not equal {}, returning {}", node1.getNodeName(), node2.getNodeName(), compare(node1.getNodeName(), node2.getNodeName()));
			return compare(node1.getNodeName(), node2.getNodeName());
		}

		NodeList children1 = node1.getChildNodes();
		NodeList children2 = node2.getChildNodes();

		LOG.trace("Does the amount of child nodes {} equal {}?", children1.getLength(), children2.getLength());
		if(children1.getLength() != children2.getLength()){
			LOG.debug("The amount of child nodes {} does not equal {}, returning {}", children1.getLength(), children2.getLength(), Integer.compare(children1.getLength(), children2.getLength()));
			return Integer.compare(children1.getLength(), children2.getLength());
		}

		LOG.trace("Do the nodes even have children?");
		if(children1.getLength() == 0) {
			LOG.debug("The nodes do not have children. Comparing values {} and {} and returning {}", node1.getNodeValue(), node2.getNodeValue(), compare(node1.getNodeValue(), node2.getNodeValue()));
			return compare(node1.getNodeValue(), node2.getNodeValue());
		}
		
		int comp = 0;
		for(int i = 0; i < children1.getLength(); i++) {
			LOG.trace("Comparing nodes at position {}", i);
			comp = compareNodes(new SortedNode(children1.item(i)), new SortedNode(children2.item(i)));
			if(comp!=0) {
				LOG.debug("Nodes at position {} were different. Returning {}", i, comp);
				return comp;
			}
		}

		LOG.debug("Thes nodes are the same. returning 0");
		return 0;
	}

	private static void sortChildren(Node parent) {
		LOG.debug("Sorting children of node {}", parent.getNodeName());
		
		LOG.debug("Adding all the children to an ArrayList");
		ArrayList<Node> nodeList = new ArrayList<>(parent.getChildNodes().getLength()/2);
		Node[] spacers = new Node[parent.getChildNodes().getLength()];
		for(int i = 0; i < parent.getChildNodes().getLength(); i++) {
			Node child = parent.getChildNodes().item(i);
			if(!isSpacer(child)) {
				nodeList.add(child);
			}
			spacers[i] = isSpacer(child)?child:null;
			LOG.debug(escapeWhitespace(child.toString()) + " is " + (spacers[i]!=null?"":"NOT") + " a spacer");
		}
		
		if(nodeList.size() == 0) {
			return;
		} else if (nodeList.size() == 1) {
			sortChildren(nodeList.get(0));
		} else {
			LOG.debug("Sorting the ArrayList");
			Collections.sort(nodeList, new Comparator<Node>() {

				@Override
				public int compare(Node o1, Node o2) {
//					return compareNodes(o1, o2);
					return compareNodes(new SortedNode(o1), new SortedNode(o2));
				}
			});

			LOG.debug("Removing all the children under " + parent);
			Node child = parent.getFirstChild();
			while(child != null) {
				parent.removeChild(child);
				child = parent.getFirstChild();
			}

			LOG.debug("Re-adding all the children in order under " + parent);
			LOG.debug(Arrays.toString(spacers));
			Iterator<Node> iter = nodeList.iterator();
			for(int i = 0; i < spacers.length; i++) {
				if(spacers[i] == null) {
					Node next = iter.next();
					LOG.debug("adding non spacer node " + next);
					parent.appendChild(next);
				} else {
					LOG.debug("adding spacer node " + parent.getChildNodes().item(i));
					parent.appendChild(spacers[i]);
				}
			}
		}
	}
	
	private static void analyzeNode(Node node, int indent) {
		StringBuilder sb = new StringBuilder(getIndent(indent));
		
		
		sb.append("[");
		if(isSpacer(node)) {
			sb.append("SPACER");
		} else {
			sb.append(getNodeTypeVal(node.getNodeType()));
		}
		sb.append("] [")
		.append(node.getNodeName())
		;
		

		sb.append("] [")
		.append(node.getChildNodes().getLength())
		.append("] children")
		;

		sb.append("] = [")
		.append(escapeWhitespace(node.getNodeValue()))
		.append("]")
		;
		
		LOG.info(sb.toString());
		Node child = node.getFirstChild();
		while(child != null) {
			analyzeNode(child, indent + 1);
			child = child.getNextSibling();
		}
	}
	
	private static String getIndent(int indent) {
		String result = "";
		for(int i = 0; i<indent; i++){
			result += " ";
		}
		return result;
	}
	
	private static String getNodeTypeVal(short nodeTypeNum) {
		switch(nodeTypeNum){
			case 1: return "ELEMENT_NODE";
			case 2: return "ATTRIBUTE_NODE";
			case 3: return "TEXT_NODE";
			case 4: return "CDATA_SECTION_NODE";
			case 5: return "ENTITY_REFERENCE_NODE";
			case 6: return "ENTITY_NODE";
			case 7: return "PROCESSING_INSTRUCTION_NODE";
			case 8: return "COMMENT_NODE";
			case 9: return "DOCUMENT_NODE";
			case 10: return "DOCUMENT_TYPE_NODE";
			case 11: return "DOCUMENT_FRAGMENT_NODE";
			case 12: return "NOTATION_NODE";
			default: return "UNKNOWN";
		}
	}
	
	private static String escapeWhitespace(String orig){
		if(null==orig) {
			return orig;
		} else {
			return orig.replace("\t", "\\t").replace("\n", "\\n");
		}
	}
	
	private static boolean isSpacer(Node node) {
		return node.getNodeValue() != null && node.getNodeValue().matches("\\n *");
	}
	
	public static class SortedNode {
		Node node;
		boolean isSorted;
		
		public SortedNode(Node node) {
			this.node = node;
		}
		
		public void sort() {
			if(isSorted) {
				LOG.info("Skipping");
				return;
			}
			XmlSorter.sortChildren(node);
			isSorted = true;
		}
	}
	
	public static int compare(String s1, String s2) {
		if(s1 == null && s2 == null) {
			return 0;
		} else if(s1 == null && s2 != null) {
			return -1;
		} else if (s2 == null && s1 != null) {
			return 1;
		} else {
			return s1.compareTo(s2);
		}
	}
}
