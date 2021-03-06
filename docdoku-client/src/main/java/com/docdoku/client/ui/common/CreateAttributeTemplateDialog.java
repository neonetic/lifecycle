/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2013 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.docdoku.client.ui.common;

import com.docdoku.client.localization.I18N;
import java.awt.*;
import java.awt.event.ActionListener;


public class CreateAttributeTemplateDialog extends AttributeTemplateDialog{

    public CreateAttributeTemplateDialog(Frame pOwner, ActionListener pAction) {
        super(pOwner, I18N.BUNDLE.getString("AttributeCreation_title"));
        init(pAction);
    }

    public CreateAttributeTemplateDialog(Dialog pOwner, ActionListener pAction) {
        super(pOwner, I18N.BUNDLE.getString("AttributeCreation_title"));
        init(pAction);
    }


    @Override
    protected void init(ActionListener pAction){
        mEditAttributePanel = new EditAttributeTemplatePanel();
        super.init(pAction);
        mOKCancelPanel.setEnabled(false);
        setVisible(true);
    }
}
