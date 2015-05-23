package com.koreinfotech.sms.streakintegration.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.koreinfotech.sms.streakintegration.DAO.ImplLeadTableDAO;
import com.koreinfotech.sms.streakintegration.DAO.LeadTableDAO;
import com.koreinfotech.sms.streakintegration.domain.LeadTable;
import com.streakapi.crm.api.IStreakAPI;
import com.streakapi.crm.datatype.Box;
import com.streakapi.crm.datatype.Field;
import com.streakapi.crm.datatype.Field.TYPE;
import com.streakapi.crm.datatype.Fields;
import com.streakapi.crm.datatype.Pipeline;
import com.streakapi.crm.datatype.Stage;
import com.streakapi.crm.datatype.Stages;
import com.streakapi.crm.exceptions.NoValidObjectsReturned;
import com.streakapi.crm.impl.StreakAPIImpl;

public class IStreakIntegrationAPI {

	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		/*		String streakKey = "";
//		String pipelineKey = "";
		String pipelineKey = "";
		String boxKey = "";*/

		String streakKey = "";
		//		String pipelineKey = "";
		//		String pipelineKey = "";
		String pipelineKey = "";
		String boxKey = "";

		//Pipelines
		List<Pipeline> pipeList = null;
		Pipeline pipeLine = null;
		List<Field> fields = null;
		Stages stagesMap = null;
		List<String> stageOrder = new ArrayList<String>();
		ArrayList<Stage> stageList = new ArrayList<Stage>();
		Map<String, String> stageMap = new HashMap<String, String>();
		ArrayList<HashMap<String, String>> personMap = new ArrayList<HashMap<String, String>>();

		//Boxes
		List<Box> boxList = null;
		Box boxObj = null;
		Fields boxFields = null;

		IStreakAPI streakAPI = new StreakAPIImpl(streakKey);
		try {

			System.out.println("\t**********************");
			System.out.println("\t GETTING DATA FROM PIPELINE(S)");
			System.out.println("\t**********************");


			pipeList = streakAPI.getAllPipelines();
			System.out.println("Number of Pipelines: " + pipeList.size());


			pipeLine = streakAPI.getPipeline(pipelineKey);
			fields = pipeLine.getFields();
			stagesMap = pipeLine.getStages();
			Map<String, Stage> allStages = stagesMap.getAllStages();

			System.out.println("Printing all the field values: ");
			System.out.println("**********************");
			for (Field field : fields) {
				System.out.println("Field Name: '" + field.getName()+"'");
				System.out.println("Field Key: '" + field.getKey()+"'");
				System.out.println("Field Type: '" + field.getType()+"'");
				System.out.println("----------");
			}
			System.out.println("**********************");

			System.out.println("Printing the order of Stages: ");
			System.out.println(pipeLine.getStageOrder());
			stageOrder = pipeLine.getStageOrder();
			System.out.println("**********************");

			System.out.println("Printing all the stages and creating a Stage ArrayList (maintaining Stage Order): ");
			System.out.println("**********************");
			int numOfStages = stageOrder.size();
			for (int i=0; i<numOfStages; i++) {
				stageList.add(i, allStages.get(stageOrder.get(i)));
				stageMap.put(stageList.get(i).getKey(), stageList.get(i).getName());
				System.out.println("Item [" + i +"] of StageList: " + stageList.get(i));
				System.out.println("----------");
			}
			System.out.println("**********************");

			Calendar lastUpdateTime = pipeLine.getLastUpdatedTimestamp();
			System.out.println("Printing Last Updated Time: ");
			System.out.println(lastUpdateTime.getTime());
			System.out.println(lastUpdateTime.getTimeInMillis());
			System.out.println("**********************");

			System.out.println("\t**********************");
			System.out.println("\t GETTING DATA FROM BOX(ES)");
			System.out.println("\t**********************");

			boxList = streakAPI.getBoxesInPipeline(pipelineKey);
			System.out.println("Number of Boxes in " + pipeLine.getName() 
					+"Pipeline: " + boxList.size());
			String fieldKey = "";
			LeadTable lead = null;
			
			ArrayList<LeadTable> leadsList = new ArrayList<LeadTable>();
			
			for (Box box : boxList) {

				System.out.println("Box Name: " + box.getName());
				System.out.println("Box Key: " + box.getKey());
				System.out.println("Box Last-updated time: " + box.getLastUpdatedTimestamp().getTime());
				//				System.out.println("Box Fields: " + box.getFields());
				boxFields = box.getFields();
				System.out.println("Box Notes: " + box.getNotes());
				System.out.println("Box Stage Key: " + box.getStageKey());
				System.out.println("Box Stage Name: " + stageMap.get(box.getStageKey()));
				
				lead = new LeadTable();
				lead.setStreakBoxKey(box.getBoxKey());
				lead.setLeadName(box.getName());
				lead.setCourseName((String)boxFields.getAllFields().get("1002"));
				lead.setPhone((String)boxFields.getAllFields().get("1003"));
				lead.setEmail((String)boxFields.getAllFields().get("1004"));
				lead.setNotes(box.getNotes());
				lead.setMeetingOn(new Date((long) boxFields.getAllFields().get("1007")));
				lead.setMeetingTime(new Date((long)boxFields.getAllFields().get("1011")));
				lead.setLastUpdateTime(box.getLastUpdatedTimestamp().getTime());
				lead.setAssignedTo((String)boxFields.getAllFields().get("1014"));
				lead.setDealSize((String)boxFields.getAllFields().get("1009"));
				
				leadsList.add(lead);
				
				for (Field field : fields) {
					System.out.print( "Field Key: '" + field.getKey()+"'"
										+ "; Field Type: '" + field.getType()+"'"
										+ "; Field Name: '" + field.getName()+"'"
										+ ";\nField value: " + boxFields.getAllFields().get(field.getKey()));
					if (field.getType() == TYPE.PERSON) {
						personMap = (ArrayList<HashMap<String, String>>) boxFields.getAllFields().get(field.getKey());
						if (personMap.size() > 0) {
							System.out.println("Field Value (Showing Only PERSON name): '"+ personMap.get(0).get("fullName")+"'");
						}
						else {
							System.out.println("Field Value is empty/unassigned");
						}
					}
					else {
						System.out.println("Field value: " + boxFields.getAllFields().get(field.getKey()));
					}
					System.out.println("----------");
				}

				/*	
				 * System.out.println("Printing the values of the fields: ");
				for (Field field : fields) {
					System.out.println("----------");
					System.out.println	( "Field Key: "+ field.getKey()
										+ "; Field Type: " + field.getType()
										+ "; Field Name: " + field.getName()
										+ ";\nField value: " + boxFields.getAllFields().get(field.getKey()));
					System.out.println("----------");
				}
				 */
				System.out.println("----------");
			}
			
			
			LeadTableDAO leadDAO = new ImplLeadTableDAO();
			for (LeadTable leadFrmList : leadsList) {
				leadDAO.addLead(leadFrmList);
			}

			//TODO must cater for JsonMappingException that could be thrown here...
			/*
			boxObj = streakAPI.getBox(boxKey);
			System.out.println("Getting values for Box : " + boxObj.getName());
			System.out.println("----------");
			System.out.println("Box Name: " + boxObj.getName());
			System.out.println("Box Key: " + boxObj.getKey());
			System.out.println("Box Name: " + boxObj.getName());
			System.out.println("Box Last-updated time: " + boxObj.getLastUpdatedTimestamp().getTime());
			System.out.println("Box Fields: " + boxObj.getFields());
			boxFields = boxObj.getFields();
			System.out.println("Box Notes: " + boxObj.getNotes());
			System.out.println("Box Stage Key: " + boxObj.getStageKey());
			System.out.println("Box Stage Name: " + stageMap.get(boxObj.getStageKey()));
			System.out.println("----------");

			System.out.println("Printing the field values for Box : " + boxObj.getName());
			System.out.println("----------");
			 */
			/*			for (Field field : fields) {
				System.out.print	( "Field Key: "+ field.getKey()
									+ "; Field Type: " + field.getType()
									+ "; Field Name: " + field.getName());
//									+ ";\nField value: " + boxFields.getAllFields().get(field.getKey()));
				if (field.getType() == TYPE.PERSON) {
					personMap = (ArrayList<HashMap<String, String>>) boxFields.getAllFields().get(field.getKey());
					System.out.println("\nField Value (Showing Only PERSON name): "+ personMap.get(0).get("fullName"));
				}
				else {
					System.out.println("Field value: " + boxFields.getAllFields().get(field.getKey()));
				}
				System.out.println("----------");
			}
			 */

			System.out.println("Printing values of all the pipelines:");
			for (Pipeline pipe : pipeList) {
				System.out.println("Pipe Name : " + pipe.getName() + ": Pipe Key: " + pipe.getKey());

			}

		} catch (NoValidObjectsReturned e) {
			e.printStackTrace();
		}
	}
}
