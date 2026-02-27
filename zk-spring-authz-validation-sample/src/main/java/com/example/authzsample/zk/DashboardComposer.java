package com.example.authzsample.zk;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zkplus.spring.SpringUtil;

public class DashboardComposer extends SelectorComposer<Component> {

    @Wire
    private Button createPaymentBtn;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        AuthorizationGuard guard = (AuthorizationGuard) SpringUtil.getBean("authorizationGuard");
        boolean allowed = guard.can("PAYMENT", "CREATE");
        createPaymentBtn.setVisible(allowed);
    }
}
