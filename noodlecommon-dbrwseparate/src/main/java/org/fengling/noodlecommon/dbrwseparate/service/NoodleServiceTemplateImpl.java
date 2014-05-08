package org.fengling.noodlecommon.dbrwseparate.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceContextHolder;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.fengling.noodlecommon.dbrwseparate.loadbalancer.DataSourceModel;
import org.fengling.noodlecommon.dbrwseparate.loadbalancer.LoadBalancerManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class NoodleServiceTemplateImpl implements NoodleServiceTemplate {

    private final static Log logger = LogFactory.getLog(NoodleServiceTemplateImpl.class);

    private TransactionTemplate transactionTemplate;
    
    private LoadBalancerManager loadBalancerManager;

    @Override
    public <T> T execute(final NoodleServiceCallback<T> action) throws NoodleServiceException, Exception {
        
        NoodleServiceCallbackExtend<T> noodleServiceCallbackExtendTemp = null;
        if (action instanceof NoodleServiceCallbackExtend) {
        	noodleServiceCallbackExtendTemp = (NoodleServiceCallbackExtend<T>) action;
        }
        final NoodleServiceCallbackExtend<T> noodleServiceCallbackExtend = noodleServiceCallbackExtendTemp;

        T result = null;
        
        try {
        	DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);

        	if (noodleServiceCallbackExtend != null) {
        		if (!noodleServiceCallbackExtend.beforeExecuteActionCheck()) {
        			if (logger.isErrorEnabled()) {
        				logger.equals("execute -> Before execute action check fail");
        			}
        			throw new NoodleServiceException("Before execute action check fail");
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
                    			serviceResult.setFailException(new NoodleServiceException("Before execute action check in transaction fail"));
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
        	DataSourceContextHolder.clearDataSourceType();
        }

        return result;
    }
    
    @Override
    public <T> T executeWithoutTransaction(final NoodleServiceCallback<T> action) throws NoodleServiceException, Exception {

        DataSourceModel mSDataSourceModel = loadBalancerManager.getAliveDataSource();
		if (mSDataSourceModel == null) {
			if (logger.isErrorEnabled()) {
				logger.equals("executeWithoutTransaction -> None of the available datasource");
			}
			throw new NoodleServiceException("None of the available datasource");
        }
		
		NoodleServiceCallbackExtend<T> noodleServiceCallbackExtendTemp = null;
        if (action instanceof NoodleServiceCallbackExtend) {
        	noodleServiceCallbackExtendTemp = (NoodleServiceCallbackExtend<T>) action;
        }
        final NoodleServiceCallbackExtend<T> noodleServiceCallbackExtend = noodleServiceCallbackExtendTemp;

        T result = null;
        
        ServiceResult serviceResult = new ServiceResult();
        
        try {
        	DataSourceContextHolder.setDataSourceType(mSDataSourceModel.getDataSourceType());
            
        	if (noodleServiceCallbackExtend != null) {
        		if (!noodleServiceCallbackExtend.beforeExecuteActionCheck()) {
        			if (logger.isErrorEnabled()) {
        				logger.equals("executeWithoutTransaction -> Before execute action check fail");
        			}
        			throw new NoodleServiceException("Before execute action check fail");
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
        	 DataSourceContextHolder.clearDataSourceType();
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
