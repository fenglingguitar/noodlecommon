package org.fengling.noodlecommon.dbrwseparate.operation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.fengling.noodlecommon.dbrwseparate.loadbalancer.LoadBalancerManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class OperationTemplateImpl implements OperationTemplate {

    private final static Log logger = LogFactory.getLog(OperationTemplateImpl.class);

    private TransactionTemplate transactionTemplate;
    
    private LoadBalancerManager loadBalancerManager;

    @Override
    public <T> T execute(final OperationCallback<T> action) throws OperationException, Exception {
        
        OperationCallbackExtend<T> noodleServiceCallbackExtendTemp = null;
        if (action instanceof OperationCallbackExtend) {
        	noodleServiceCallbackExtendTemp = (OperationCallbackExtend<T>) action;
        }
        final OperationCallbackExtend<T> noodleServiceCallbackExtend = noodleServiceCallbackExtendTemp;

        T result = null;
        
        try {
        	DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);

        	if (noodleServiceCallbackExtend != null) {
        		if (!noodleServiceCallbackExtend.beforeExecuteActionCheck()) {
        			if (logger.isErrorEnabled()) {
        				logger.equals("execute -> Before execute action check fail");
        			}
        			throw new OperationException("Before execute action check fail");
        		}
        		noodleServiceCallbackExtend.beforeExecuteAction();
        	}
        	
        	final ServiceResult serviceResult = new ServiceResult();
        	
        	try {
        		result = this.transactionTemplate.execute(new TransactionCallback<T>() {
                	
                    public T doInTransaction(TransactionStatus status) {

                    	T result = null;
                    	
                    	if (noodleServiceCallbackExtend != null) {
                    		if (!noodleServiceCallbackExtend.beforeExecuteActionCheckInTransaction()) {
                    			if (logger.isErrorEnabled()) {
                    				logger.equals("execute -> Before execute action check in transaction fail");
                    			}
                    			serviceResult.setSuccess(false);
                    			serviceResult.setFailException(new OperationException("Before execute action check in transaction fail"));
                    			return result;          
                    		}
                    		noodleServiceCallbackExtend.beforeExecuteActionInTransaction();
                    	}

        				try {
        					result = action.executeAction();
        				} catch (Exception e) {
        					if (logger.isErrorEnabled()) {
                				logger.equals("execute -> Execute action exception, Exception: " + e);
                			}
        					serviceResult.setSuccess(false);
        					serviceResult.setFailException(e);
        				}

                        if (noodleServiceCallbackExtend != null) {
                    		noodleServiceCallbackExtend.afterExecuteActionInTransaction(serviceResult.isSuccess(), result, serviceResult.getFailException());
                    	}
                        
                        if (!serviceResult.isSuccess()) {
                        	status.setRollbackOnly();
                        }
                        
        				return result;                         
                    }
                });
        	} catch (TransactionException e) {
        		if (logger.isErrorEnabled()) {
    				logger.equals("execute -> Transaction exception, Exception: " + e);
    			}
        		serviceResult.setSuccess(false);
        		serviceResult.setFailException(e);
        	}
        	
            if (noodleServiceCallbackExtend != null) {
        		noodleServiceCallbackExtend.afterExecuteAction(serviceResult.isSuccess(), result, serviceResult.getFailException());
        	}
            
            if (!serviceResult.isSuccess()) {
            	throw serviceResult.getFailException();
            }
        } finally {
        	DataSourceSwitch.clearDataSourceType();
        }

        return result;
    }
    
    @Override
    public <T> T executeWithoutTransaction(final OperationCallback<T> action) throws OperationException, Exception {

    	DataSourceType dataSourceType = loadBalancerManager.getAliveDataSource();
		if (dataSourceType == null) {
			if (logger.isErrorEnabled()) {
				logger.equals("executeWithoutTransaction -> None of the available datasource");
			}
			throw new OperationException("None of the available datasource");
        }
		
		OperationCallbackExtend<T> noodleServiceCallbackExtendTemp = null;
        if (action instanceof OperationCallbackExtend) {
        	noodleServiceCallbackExtendTemp = (OperationCallbackExtend<T>) action;
        }
        final OperationCallbackExtend<T> noodleServiceCallbackExtend = noodleServiceCallbackExtendTemp;

        T result = null;
        
        ServiceResult serviceResult = new ServiceResult();
        
        try {
        	DataSourceSwitch.setDataSourceType(dataSourceType);
            
        	if (noodleServiceCallbackExtend != null) {
        		if (!noodleServiceCallbackExtend.beforeExecuteActionCheck()) {
        			if (logger.isErrorEnabled()) {
        				logger.equals("executeWithoutTransaction -> Before execute action check fail");
        			}
        			throw new OperationException("Before execute action check fail");
        		}
        		noodleServiceCallbackExtend.beforeExecuteAction();
        	}
            
        	try {
				result = action.executeAction();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
    				logger.equals("executeWithoutTransaction -> Execute action exception, Exception: " + e);
    			}
				serviceResult.setSuccess(false);
        		serviceResult.setFailException(e);
			}
        	
            if (noodleServiceCallbackExtend != null) {
        		noodleServiceCallbackExtend.afterExecuteAction(serviceResult.isSuccess(), result, serviceResult.getFailException());
        	}
            
            if (!serviceResult.isSuccess()) {
            	throw serviceResult.getFailException();
            }
        } finally{
        	 DataSourceSwitch.clearDataSourceType();
        }
        
        return result;
    }
    
    private class ServiceResult {
    	
    	private boolean isSuccess = true;
    	private Exception failException = null;
    	
		public boolean isSuccess() {
			return isSuccess;
		}
		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}
		public Exception getFailException() {
			return failException;
		}
		public void setFailException(Exception failException) {
			this.failException = failException;
		}
    }
    
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

	public void setLoadBalancerManager(LoadBalancerManager loadBalancerManager) {
		this.loadBalancerManager = loadBalancerManager;
	}
}
