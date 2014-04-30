
package org.fengling.noodlecommon.dbrwseparate.service;

import org.springframework.transaction.support.TransactionTemplate;


public interface TuanServiceTemplate {

	TuanCallbackResult execute(TuanServiceCallback action, Object domain);
    TuanCallbackResult executeWithoutTransaction(TuanServiceCallback action, Object domain);
    TuanCallbackResult executeWithoutTransactionCheckOrder(TuanServiceCallback action, Object domain);
    TuanCallbackResult executeSubmeter(TuanServiceCallback action, Object domain , TransactionTemplate transactionTemplate) ;
}
