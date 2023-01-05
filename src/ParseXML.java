/*
ParseXML - This class parses all data needed for deadwood. It is designed to parse from
board.xml and cards.xml 

Dominic Danis 11/2/2021
*/

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.ArrayList;

public class ParseXML {
    
    public Document getDocFromFile(String filename)
        throws ParserConfigurationException{
        {
            
                  
           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
           DocumentBuilder db = dbf.newDocumentBuilder();
           Document doc = null;
           
           try{
               doc = db.parse(filename);
           } catch (Exception ex){
               System.out.println("XML parse failure");
               ex.printStackTrace();
           }
           return doc;
        } // exception handling
        
        }

        //parses card data - returns arrayList of all scene data
        public ArrayList<Scenes> readCardData(Document d){
            Element root = d.getDocumentElement();
            NodeList cards = root.getElementsByTagName("card");

            ArrayList<Scenes> returnScenes = new ArrayList<Scenes>();

            for(int i=0; i<cards.getLength(); i++){
                Node card = cards.item(i);
                ArrayList<Role> roles = new ArrayList<Role>();
                int sceneNumber = 0;                                                //set holders for class constructors
                String sceneDescription = "";
                String partName;
                int partRank;
                String line = "";
                String cardName = card.getAttributes().getNamedItem("name").getNodeValue();        //fill holders     
                String img = card.getAttributes().getNamedItem("img").getNodeValue();
                int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());

                NodeList children = card.getChildNodes();

                for(int j=0; j<children.getLength(); j++){
                    Node sub = children.item(j);

                    if("scene".equals(sub.getNodeName())){                              //fill scene information
                        sceneNumber = Integer.parseInt(sub.getAttributes().getNamedItem("number").getNodeValue());
                        sceneDescription = sub.getTextContent();
                    }
                    else if("part".equals(sub.getNodeName())){                          //fill role information
                        ArrayList<Integer> dimensions = new ArrayList<Integer>();
                        partName = sub.getAttributes().getNamedItem("name").getNodeValue();
                        partRank = Integer.parseInt(sub.getAttributes().getNamedItem("level").getNodeValue());
                        NodeList partChildren = sub.getChildNodes();
                        for(int k=0; k<partChildren.getLength(); k++){
                            Node pSub = partChildren.item(k);
                            if("area".equals(pSub.getNodeName())){
                                dimensions.add(Integer.parseInt(pSub.getAttributes().getNamedItem("x").getNodeValue()));
                                dimensions.add(Integer.parseInt(pSub.getAttributes().getNamedItem("y").getNodeValue()));
                                dimensions.add(Integer.parseInt(pSub.getAttributes().getNamedItem("h").getNodeValue()));
                                dimensions.add(Integer.parseInt(pSub.getAttributes().getNamedItem("w").getNodeValue()));
                            }
                            else if("line".equals(pSub.getNodeName())){
                                line = pSub.getTextContent();
                            }
                            else{};
                        }
                        Role entry = new Role(partName, partRank, dimensions, line, true);       //construct role
                        roles.add(entry);                                                  //add to list of roles
                    }
                    else{};
                }
                Scenes addScene = new Scenes(sceneNumber, sceneDescription, cardName, img, budget, roles);    //construct scene
                returnScenes.add(addScene);                                                 //add to list of scenes
            }
            return returnScenes;
        }

        //prarses board data - return arraylist of all location, with roles, scenes, upgrades, takes
        public ArrayList<Location> readBoardData(Document d){
            ArrayList<Location> returnLocations = new ArrayList<Location>();
            Element root = d.getDocumentElement();
            NodeList locations = root.getElementsByTagName("set");                  //data related to sets
            for(int i=0;i<locations.getLength();i++){
                Node location = locations.item(i);
                String locName = location.getAttributes().getNamedItem("name").getNodeValue();
                NodeList children = location.getChildNodes();
                ArrayList<String> neighbors = new ArrayList<String>();              //containers to be filled
                ArrayList<Integer> area = new ArrayList<Integer>();
                ArrayList<Take> takes = new ArrayList<Take>();
                ArrayList<Role> roles = new ArrayList<Role>();
                for(int j=0; j<children.getLength(); j++){
                    Node sub = children.item(j);            
                    if("neighbors".equals(sub.getNodeName())){                      //set neighbors
                        NodeList adj = sub.getChildNodes();
                        for(int k=0; k<adj.getLength(); k++){
                            Node neigh = adj.item(k);
                            if("neighbor".equals(neigh.getNodeName())){
                                neighbors.add(neigh.getAttributes().getNamedItem("name").getNodeValue());
                            }
                            else{}
                        }
                    }
                    else if("area".equals(sub.getNodeName())){                      //set area
                        area.add(Integer.parseInt(sub.getAttributes().getNamedItem("x").getNodeValue()));
                        area.add(Integer.parseInt(sub.getAttributes().getNamedItem("y").getNodeValue()));
                        area.add(Integer.parseInt(sub.getAttributes().getNamedItem("h").getNodeValue()));
                        area.add(Integer.parseInt(sub.getAttributes().getNamedItem("w").getNodeValue()));
                    }
                    else if("parts".equals(sub.getNodeName())){                     //parts on set + details
                        NodeList rls = sub.getChildNodes();
                        for(int x=0; x<rls.getLength(); x++){
                            Node role = rls.item(x);
                            String partName = "";
                            int rank = 0;
                            String line = "";
                            ArrayList<Integer> roleArea = new ArrayList<Integer>();
                            if("part".equals(role.getNodeName())){
                                partName = role.getAttributes().getNamedItem("name").getNodeValue();
                                rank = Integer.parseInt(role.getAttributes().getNamedItem("level").getNodeValue());
                            }
                            NodeList roleAtts = role.getChildNodes();
                            for(int z=0; z<roleAtts.getLength(); z++){
                                Node roleTm = roleAtts.item(z);
                                if("area".equals(roleTm.getNodeName())){
                                    roleArea.clear();
                                    roleArea.add(Integer.parseInt(roleTm.getAttributes().getNamedItem("x").getNodeValue()));
                                    roleArea.add(Integer.parseInt(roleTm.getAttributes().getNamedItem("y").getNodeValue()));
                                    roleArea.add(Integer.parseInt(roleTm.getAttributes().getNamedItem("h").getNodeValue()));
                                    roleArea.add(Integer.parseInt(roleTm.getAttributes().getNamedItem("w").getNodeValue())); 
                                }
                                else if("line".equals(roleTm.getNodeName())){
                                    line = roleTm.getTextContent();
                                }

                            }
                            if(!partName.equals("")){
                                Role entryRole = new Role(partName, rank, roleArea, line);
                                roles.add(entryRole);
                            }
                        }
                        

                    }
                    else if("takes".equals(sub.getNodeName())){                     //takes a set has
                        NodeList takeList = sub.getChildNodes();
                        for(int q=0; q<takeList.getLength(); q++){
                            ArrayList<Integer> dims = new ArrayList<Integer>();
                            int takeNum = 0;
                            Node takeNode = takeList.item(q);
                            if("take".equals(takeNode.getNodeName())){
                                takeNum = Integer.parseInt(takeNode.getAttributes().getNamedItem("number").getNodeValue());
                                NodeList dimList = takeNode.getChildNodes();
                                for(int a=0;a<dimList.getLength();a++){
                                    Node dimTag = dimList.item(a);
                                    dims.add(Integer.parseInt(dimTag.getAttributes().getNamedItem("x").getNodeValue()));
                                    dims.add(Integer.parseInt(dimTag.getAttributes().getNamedItem("y").getNodeValue()));
                                    dims.add(Integer.parseInt(dimTag.getAttributes().getNamedItem("h").getNodeValue()));
                                    dims.add(Integer.parseInt(dimTag.getAttributes().getNamedItem("w").getNodeValue()));
                                }
                                Take midTake = new Take(takeNum,dims);
                                takes.add(midTake);
                            }
                        }
                    }
                }
                Location addLoc = new Location(neighbors, locName, takes, roles, area); //construct location with details
                returnLocations.add(addLoc);                                            //add to our return list
            }
            
            NodeList trail = root.getElementsByTagName("trailer");                      //get trailer data
            for(int i=0;i<trail.getLength();i++){
                ArrayList<String> neighbors = new ArrayList<String>();
                ArrayList<Integer> area = new ArrayList<Integer>();
                Node trailer = trail.item(i);
                NodeList trailSet = trailer.getChildNodes();
                for(int j=0; j<trailSet.getLength(); j++){
                    Node trailTime = trailSet.item(j);
                    if("neighbors".equals(trailTime.getNodeName())){
                        NodeList adj = trailTime.getChildNodes();
                        for(int k=0; k<adj.getLength(); k++){
                            Node neigh = adj.item(k);
                            if("neighbor".equals(neigh.getNodeName())){
                                neighbors.add(neigh.getAttributes().getNamedItem("name").getNodeValue());
                            }
                            else{}
                        }
                    }
                    else if("area".equals(trailTime.getNodeName())){
                        area.add(Integer.parseInt(trailTime.getAttributes().getNamedItem("x").getNodeValue()));
                        area.add(Integer.parseInt(trailTime.getAttributes().getNamedItem("y").getNodeValue()));
                        area.add(Integer.parseInt(trailTime.getAttributes().getNamedItem("h").getNodeValue()));
                        area.add(Integer.parseInt(trailTime.getAttributes().getNamedItem("w").getNodeValue()));
                    }
                    else{}
                }
                
                String name = "trailer";
                Location trailLoc = new Location(neighbors, name, area);
                returnLocations.add(trailLoc);                                      //add trailer to the list
            }

            NodeList upgrades = root.getElementsByTagName("office");                //get office data
            for(int i=0; i<upgrades.getLength(); i++){
                ArrayList<Integer> locArea = new ArrayList<Integer>();
                ArrayList<String> neighbors= new ArrayList<String>();
                ArrayList<Upgrade> availUpgrades = new ArrayList<Upgrade>();
                String name = "office";
                Node upgrade = upgrades.item(i);
                NodeList upgradeChil = upgrade.getChildNodes();
                for(int j=0;j<upgradeChil.getLength();j++){
                    Node neighbs = upgradeChil.item(j);
                    if("neighbors".equals(neighbs.getNodeName())){
                        NodeList adj = neighbs.getChildNodes();
                        for(int k=0; k<adj.getLength(); k++){
                            Node neigh = adj.item(k);
                            if("neighbor".equals(neigh.getNodeName())){
                                neighbors.add(neigh.getAttributes().getNamedItem("name").getNodeValue());
                            }
                            else{}
                        }
                    }
                    else if("area".equals(neighbs.getNodeName())){
                        locArea.add(Integer.parseInt(neighbs.getAttributes().getNamedItem("x").getNodeValue()));
                        locArea.add(Integer.parseInt(neighbs.getAttributes().getNamedItem("y").getNodeValue()));
                        locArea.add(Integer.parseInt(neighbs.getAttributes().getNamedItem("h").getNodeValue()));
                        locArea.add(Integer.parseInt(neighbs.getAttributes().getNamedItem("w").getNodeValue()));
                    }
                    else if("upgrades".equals(neighbs.getNodeName())){
                        NodeList ups = neighbs.getChildNodes();
                        for(int q =0; q<ups.getLength(); q++){
                            int rank = 0;
                            String currency = "";
                            ArrayList<Integer> area = new ArrayList<Integer>();
                            int curAmt = 0;
                            Node curUp = ups.item(q);
                            if("upgrade".equals(curUp.getNodeName())){
                                rank = Integer.parseInt(curUp.getAttributes().getNamedItem("level").getNodeValue());
                                currency = curUp.getAttributes().getNamedItem("currency").getNodeValue();
                                curAmt = Integer.parseInt(curUp.getAttributes().getNamedItem("amt").getNodeValue());
                                NodeList areas = curUp.getChildNodes();
                                for(int k=0;k<areas.getLength();k++){
                                    Node upArea = areas.item(k);
                                    if("area".equals(upArea.getNodeName())){
                                        area.add(Integer.parseInt(upArea.getAttributes().getNamedItem("x").getNodeValue()));
                                        area.add(Integer.parseInt(upArea.getAttributes().getNamedItem("y").getNodeValue()));
                                        area.add(Integer.parseInt(upArea.getAttributes().getNamedItem("h").getNodeValue()));
                                        area.add(Integer.parseInt(upArea.getAttributes().getNamedItem("w").getNodeValue()));
                                    }
                                }
                                Upgrade midUp = new Upgrade(rank,currency,area,curAmt);
                                availUpgrades.add(midUp);
                            }
                            else{}
                        }
                    }
                }
                Location officeLoc = new Location(name, neighbors, locArea, availUpgrades);
                returnLocations.add(officeLoc);                                 //add office to our locations
            }

            return returnLocations;
        }
    }