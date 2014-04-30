
package org.fengling.noodlecommon.dbrwseparate.service;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceContextHolder;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.fengling.noodlecommon.dbrwseparate.msloadbalancer.MSDataSourceModel;
import org.fengling.noodlecommon.dbrwseparate.msloadbalancer.MSDataSourcesLoadBalancerManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class TuanServiceTemplateImpl implements TuanServiceTemplate {

    private final static Log logger = LogFactory.getLog(TuanServiceTemplateImpl.class);

    protected TransactionTemplate transactionTemplate;

    public TuanCallbackResult execute(final TuanServiceCallback action, final Object domain) {

        if (logger.isDebugEnabled()) {
            logger.debug("进入模板方法开始处理");
        }
        
        TuanCallbackResult result = TuanCallbackResult.success();

        try {
        	 // 设置数据源，读写分离
        	DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
        	result = action.executeCheck();
            if (result.isSuccess()) {
                result = (TuanCallbackResult) this.transactionTemplate.execute(new TransactionCallback<TuanCallbackResult>() {
                        public TuanCallbackResult doInTransaction(TransactionStatus status) {

                            // 3. 回调业务逻辑
                            // 3.1 通过annotation来实现某些option类型的扩展
                            TuanCallbackResult iNresult = action.executeAction();
                            if (null == iNresult) {
                                throw new TuanServiceException(TuanServiceConstants.SERVICE_NO_RESULT);
                            }

                            // 4. 扩展点
                            templateExtensionInTransaction(iNresult);
                            if (iNresult.isFailure()) {
                                status.setRollbackOnly();
                                return iNresult;
                            }
                            return iNresult;
                        }
                        
                    });
               
                if (result.isSuccess()) {
                    templateExtensionAfterTransaction(result);
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("正常退出模板方法");
            }
        } catch (TuanServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("异常退出模板方法A点", e);
            }
            result = TuanCallbackResult.failure(e.getErrorCode(), e);
        } catch (RuntimeException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("异常退出模板方法B点", e);
            }
            result = TuanCallbackResult.failure(0, e);
        } catch (Throwable e) {
           
            if (logger.isErrorEnabled()) {
                logger.error("异常退出模板方法C点", e);
            }
            result = TuanCallbackResult.failure(TuanServiceConstants.SERVICE_SYSTEM_FALIURE, e);
        }finally{
            DataSourceContextHolder.clearDataSourceType();
        }
        
        return result;
    }
    
    public TuanCallbackResult executeWithoutTransaction(final TuanServiceCallback action, final Object domain) {

        if (logger.isDebugEnabled()) {
            logger.debug("进入模板方法开始处理");
        }
        TuanCallbackResult result = TuanCallbackResult.success();
        // 设置数据源，读写分离
        
        MSDataSourceModel mSDataSourceModel = MSDataSourcesLoadBalancerManager.getAliveMSDataSource();
        
        if(mSDataSourceModel==null){
        	return TuanCallbackResult.failure(TuanServiceConstants.NO_ALIVE_DATASOURCE);
        }

        try {
        	DataSourceContextHolder.setDataSourceType(mSDataSourceModel.getDataSourceType());
            result = action.executeCheck();
            if (result.isSuccess()) {
                result = action.executeAction();
                if (null == result) {
                    throw new TuanServiceException(TuanServiceConstants.SERVICE_NO_RESULT);
                }

                // 4. 扩展点
                templateExtensionAfterExecute(result);
                if (result.isFailure()) {
                    return result;
                }
                // 5. 发送业务事件
                
            }
        } catch (TuanServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("异常退出模板方法D点", e);
            }
            result = TuanCallbackResult.failure(e.getErrorCode(), e);

        } catch (Throwable e) {
            // 把系统异常转换为服务异常
            if (logger.isErrorEnabled()) {
                logger.error("异常退出模板方法F点", e);
            }
            result = TuanCallbackResult.failure(TuanServiceConstants.SERVICE_SYSTEM_FALIURE, e);
        }finally{
        	 DataSourceContextHolder.clearDataSourceType();
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("模板执行结束");
        }
       
        return result;
    }
    
    public TuanCallbackResult executeWithoutTransactionCheckOrder(final TuanServiceCallback action, final Object domain) {

        if (logger.isDebugEnabled()) {
            logger.debug("进入模板方法开始处理");
        }
        
        TuanCallbackResult result = TuanCallbackResult.success();
        // 设置数据源，读写分离
        
        MSDataSourceModel mSDataSourceModel = MSDataSourcesLoadBalancerManager.getAliveMSDataSource();
        
        if(mSDataSourceModel==null){
        	return TuanCallbackResult.failure(TuanServiceConstants.NO_ALIVE_DATASOURCE);
        }
        
        try {
        	//DataSourceContextHolder.setDataSourceType(mSDataSourceModel.getDataSourceType());
        	DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
            result = action.executeCheck();
        
            if (result.isSuccess()) {
                result = action.executeAction();
                if (null == result) {
                    throw new TuanServiceException(TuanServiceConstants.SERVICE_NO_RESULT);
                }

                // 4. 扩展点
                templateExtensionAfterExecute(result);
                if (result.isFailure()) {
                    return result;
                }
                // 5. 发送业务事件
                
            }
        } catch (TuanServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("异常退出模板方法D点", e);
            }
            result = TuanCallbackResult.failure(e.getErrorCode(), e);
        } catch (Throwable e) {
            // 把系统异常转换为服务异常
            if (logger.isErrorEnabled()) {
                logger.error("异常退出模板方法F点", e);
            }
            result = TuanCallbackResult.failure(TuanServiceConstants.SERVICE_SYSTEM_FALIURE, e);
        } finally{
        	 DataSourceContextHolder.clearDataSourceType();
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("模板执行结束");
        }
       
        return result;
    }
    
    
    /**
     * 分表事务专用--前提，事务中操作的表在同一个库
     */
    public TuanCallbackResult executeSubmeter(final TuanServiceCallback action, final Object domain , final TransactionTemplate transactionTemplate) {

        if (logger.isDebugEnabled()) {
            logger.debug("进入模板方法开始处理");
        }
        TuanCallbackResult result = TuanCallbackResult.success();
       
        try {
             // 设置数据源，读写分离
            DataSourceContextHolder.setDataSourceType(DataSourceType.MASTER);
            result = action.executeCheck();
            if (result.isSuccess()) {
                result = (TuanCallbackResult) transactionTemplate.execute(new TransactionCallback<TuanCallbackResult>() {
                        public TuanCallbackResult doInTransaction(TransactionStatus status) {

                            // 3. 回调业务逻辑
                            // 3.1 通过annotation来实现某些option类型的扩展
                            TuanCallbackResult iNresult = action.executeAction();
                            if (null == iNresult) {
                                throw new TuanServiceException(TuanServiceConstants.SERVICE_NO_RESULT);
                            }

                            // 4. 扩展点
                            templateExtensionInTransaction(iNresult);
                            if (iNresult.isFailure()) {
                                status.setRollbackOnly();
                                return iNresult;
                            }
                            return iNresult;
                        }
                    });
               
                if (result.isSuccess()) {
                    templateExtensionAfterTransaction(result);
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("正常退出模板方法");
            }
        } catch (TuanServiceException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("异常退出模板方法A点", e);
            }
            result = TuanCallbackResult.failure(e.getErrorCode(), e);
        } catch (RuntimeException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("异常退出模板方法B点", e);
            }
            result = TuanCallbackResult.failure(0, e);
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("异常退出模板方法C点", e);
            }
            result = TuanCallbackResult.failure(TuanServiceConstants.SERVICE_SYSTEM_FALIURE, e);
        }finally{
            DataSourceContextHolder.clearDataSourceType();
        }
        
        return result;
    }
    
    /**
     * 扩展点：模板提供的允许不同类型业务在<b>事务内</b>进行扩展的一个点
     */
    protected void templateExtensionInTransaction(TuanCallbackResult result) {
    }

   
    protected void templateExtensionAfterTransaction(TuanCallbackResult result) {
    }

   
    protected void templateExtensionAfterExecute(TuanCallbackResult result) {
    }
    
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }
}
