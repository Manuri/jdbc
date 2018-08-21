/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.ballerinalang.net.jms.nativeimpl.endpoint.session;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.connector.api.BLangConnectorSPIUtil;
import org.ballerinalang.connector.api.Struct;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.natives.annotations.ReturnType;
import org.ballerinalang.net.jms.AbstractBlockingAction;
import org.ballerinalang.net.jms.Constants;
import org.ballerinalang.net.jms.utils.BallerinaAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * Create Text JMS Message.
 */
@BallerinaFunction(orgName = "ballerina", packageName = "jms",
                   functionName = "createTemporaryQueue",
                   receiver = @Receiver(type = TypeKind.OBJECT, structType = "Session",
                                        structPackage = "ballerina/jms"),
                   returnType = {
                           @ReturnType(type = TypeKind.OBJECT, structPackage = "ballerina/jms",
                                        structType = "Destination")
                   },
                   isPublic = true)
public class CreateTemporaryQueue extends AbstractBlockingAction {

    public static final Logger LOGGER = LoggerFactory.getLogger(CreateTemporaryQueue.class);

    @Override
    public void execute(Context context, CallableUnitCallback callableUnitCallback) {

        Queue jmsDestination;
        Struct sessionBObject = BallerinaAdapter.getReceiverObject(context);
        Session session = BallerinaAdapter.getNativeObject(sessionBObject, Constants.JMS_SESSION, Session.class,
                                                           context);
        BMap<String, BValue> bStruct = BLangConnectorSPIUtil.createBStruct(context, Constants.BALLERINA_PACKAGE_JMS,
                                                              Constants.JMS_DESTINATION_STRUCT_NAME);
        try {
            jmsDestination = session.createTemporaryQueue();
            bStruct.addNativeData(Constants.JMS_DESTINATION_OBJECT, jmsDestination);
            bStruct.put(Constants.DESTINATION_NAME, new BString(jmsDestination.getQueueName()));
            bStruct.put(Constants.DESTINATION_TYPE, new BString("queue"));
        } catch (JMSException e) {
            BallerinaAdapter.returnError("Failed to create temporary destination.", context, e);
        }
        context.setReturnValues(bStruct);
    }
}
