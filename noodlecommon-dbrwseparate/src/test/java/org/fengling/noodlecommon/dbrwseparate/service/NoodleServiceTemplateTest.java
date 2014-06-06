package org.fengling.noodlecommon.dbrwseparate.service;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {
		"classpath:org/fengling/noodlecommon/dbrwseparate/service/noodlecommon-service.xml"
})
public class NoodleServiceTemplateTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	NoodleServiceTemplate noodleServiceTemplate;
	
	@Autowired
	DataSource dataSource;
	
	@Test
	public void testExecute() throws Exception {
		
		int result = this.noodleServiceTemplate.execute(new NoodleServiceCallback<Integer>() {
			public Integer executeAction() throws NoodleServiceException, Exception {
				PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("insert into dbrwseparate_test (name) values (?)");
				preparedStatement.setString(1, "你好");
				return preparedStatement.executeUpdate();
			}
		});
		assertTrue(result > 0);
		
		result = this.noodleServiceTemplate.execute(new NoodleServiceCallbackExtend<Integer>() {
			public Integer executeAction() throws NoodleServiceException, Exception {
				System.out.println("NoodleServiceCallbackExtend -> executeAction...");
				PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("insert into dbrwseparate_test (name) values (?)");
				preparedStatement.setString(1, "你好");
				return preparedStatement.executeUpdate();
			}

			@Override
			public boolean beforeExecuteActionCheck() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteActionCheck...");
				return true;
			}

			@Override
			public void beforeExecuteAction() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteAction...");	
			}

			@Override
			public boolean beforeExecuteActionCheckInTransaction() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteActionCheckInTransaction...");	
				return true;
			}

			@Override
			public void beforeExecuteActionInTransaction() {	
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteActionInTransaction...");
			}

			@Override
			public void afterExecuteActionInTransaction(boolean isSuccess, Integer result, Exception e) {				
				System.out.println("NoodleServiceCallbackExtend -> afterExecuteActionInTransaction...");
			}

			@Override
			public void afterExecuteAction(boolean isSuccess, Integer result, Exception e) {
				System.out.println("NoodleServiceCallbackExtend -> afterExecuteAction...");		
			}
		});
		assertTrue(result > 0);
	}

	@Test
	public void testExecuteWithoutTransaction() throws Exception {
		List<TestModel> testModelList = this.noodleServiceTemplate.executeWithoutTransaction(new NoodleServiceCallback<List<TestModel>>() {
			public List<TestModel> executeAction() throws NoodleServiceException, Exception {
				PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("select * from dbrwseparate_test");
				ResultSet resultSet = preparedStatement.executeQuery();
				List<TestModel> testModelList = new ArrayList<TestModel>();
				while (resultSet.next()) {
					TestModel testModel = new TestModel();
					testModel.setId(resultSet.getInt(1));
					testModel.setName(resultSet.getString(2));
					testModelList.add(testModel);
				}
				return testModelList;
			}
		});
		assertNotNull(testModelList);
		for (TestModel testModel : testModelList) {
			System.out.println(testModel);
		}
		
		testModelList = this.noodleServiceTemplate.executeWithoutTransaction(new NoodleServiceCallbackExtend<List<TestModel>>() {
			public List<TestModel> executeAction() throws NoodleServiceException, Exception {
				PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("select * from dbrwseparate_test");
				ResultSet resultSet = preparedStatement.executeQuery();
				List<TestModel> testModelList = new ArrayList<TestModel>();
				while (resultSet.next()) {
					TestModel testModel = new TestModel();
					testModel.setId(resultSet.getInt(1));
					testModel.setName(resultSet.getString(2));
					testModelList.add(testModel);
				}
				return testModelList;
			}

			@Override
			public boolean beforeExecuteActionCheck() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteActionCheck...");
				return true;
			}

			@Override
			public void beforeExecuteAction() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteAction...");
			}

			@Override
			public boolean beforeExecuteActionCheckInTransaction() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteActionCheckInTransaction...");
				return true;
			}

			@Override
			public void beforeExecuteActionInTransaction() {
				System.out.println("NoodleServiceCallbackExtend -> beforeExecuteActionInTransaction...");
			}

			@Override
			public void afterExecuteActionInTransaction(boolean isSuccess, List<TestModel> result, Exception e) {
				System.out.println("NoodleServiceCallbackExtend -> afterExecuteActionInTransaction...");
			}

			@Override
			public void afterExecuteAction(boolean isSuccess, List<TestModel> result, Exception e) {
				System.out.println("NoodleServiceCallbackExtend -> afterExecuteAction...");
			}
		});
		assertNotNull(testModelList);
	}
	
	class TestModel {
		
		private Integer id;
		private String name;
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public String toString() {
			return (new StringBuilder())
					.append("[id=")
					.append(id)
					.append(", name=")
					.append(name)
					.append("]")
					.toString();
		}
	}
}
