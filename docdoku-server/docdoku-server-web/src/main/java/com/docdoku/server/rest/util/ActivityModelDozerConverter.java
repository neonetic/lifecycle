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

package com.docdoku.server.rest.util;

import org.dozer.DozerConverter;
import com.docdoku.core.workflow.ActivityModel;
import com.docdoku.core.workflow.ParallelActivityModel;
import com.docdoku.core.workflow.SerialActivityModel;
import com.docdoku.core.workflow.TaskModel;
import com.docdoku.server.rest.dto.ActivityModelDTO;
import com.docdoku.server.rest.dto.TaskModelDTO;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.List;


public class ActivityModelDozerConverter extends DozerConverter<ActivityModel, ActivityModelDTO> {

    private Mapper mapper;

    public ActivityModelDozerConverter() {
        super(ActivityModel.class, ActivityModelDTO.class);
        mapper = DozerBeanMapperSingletonWrapper.getInstance();
    }

    @Override
    public ActivityModelDTO convertTo(ActivityModel activityModel, ActivityModelDTO activityModelDTO) {

        List<TaskModelDTO> taskModelsDTO = new ArrayList<TaskModelDTO>();
        for(int i=0; i<activityModel.getTaskModels().size(); i++){
            taskModelsDTO.add(mapper.map(activityModel.getTaskModels().get(i), TaskModelDTO.class));
        }

        ActivityModelDTO.Type type;
        Integer tasksToComplete = null;

        if (activityModel instanceof SerialActivityModel) {
            type = ActivityModelDTO.Type.SERIAL;
        } else if (activityModel instanceof ParallelActivityModel) {
            type = ActivityModelDTO.Type.PARALLEL;
            tasksToComplete = ((ParallelActivityModel) activityModel).getTasksToComplete();
        } else {
            throw new IllegalArgumentException("ActivityModel type not supported");
        }

        return new ActivityModelDTO(activityModel.getStep(), taskModelsDTO, activityModel.getLifeCycleState(), type, tasksToComplete);
    }

    @Override
    public ActivityModel convertFrom(ActivityModelDTO activityModelDTO, ActivityModel activityModel) {

        List<TaskModel> taskModels = new ArrayList<TaskModel>();
        for(int i=0; i<activityModelDTO.getTaskModels().size(); i++){
            taskModels.add(mapper.map(activityModelDTO.getTaskModels().get(i), TaskModel.class));
        }

        switch (activityModelDTO.getType()){
            case SERIAL:{
                SerialActivityModel serialActivityModel = new SerialActivityModel();

                serialActivityModel.setStep(activityModelDTO.getStep());
                serialActivityModel.setTaskModels(taskModels);
                serialActivityModel.setLifeCycleState(activityModelDTO.getLifeCycleState());

                return serialActivityModel;
            }
            case PARALLEL:{
                ParallelActivityModel parallelActivityModel = new ParallelActivityModel();

                parallelActivityModel.setStep(activityModelDTO.getStep());
                parallelActivityModel.setTaskModels(taskModels);
                parallelActivityModel.setLifeCycleState(activityModelDTO.getLifeCycleState());
                parallelActivityModel.setTasksToComplete(activityModelDTO.getTasksToComplete());

                return parallelActivityModel;
            }
            default:{
                throw new IllegalArgumentException("ActivityModelDTO type not supported");
            }
        }
    }
}
