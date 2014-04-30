
package org.fengling.noodlecommon.dbrwseparate.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractServiceImpl {

    protected final Log logger = LogFactory.getLog(this.getClass());

    protected TuanServiceTemplate tuanServiceTemplate;

    public void setTuanServiceTemplate(TuanServiceTemplate tuanServiceTemplate) {
        this.tuanServiceTemplate = tuanServiceTemplate;
    }
}
