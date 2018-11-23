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
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.ballerinax.test.jdbc.transaction;

import org.ballerinalang.launcher.util.BCompileUtil;
import org.ballerinalang.launcher.util.BRunUtil;
import org.ballerinalang.launcher.util.CompileResult;
import org.ballerinalang.model.values.BInteger;
import org.ballerinalang.model.values.BValue;
import org.ballerinax.test.utils.SQLDBUtils;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

/**
 * Class to test functionality of distributed transactions in SQL.
 */
public class SQLXATransactionsTest {
    private CompileResult result;
    private static final String DB_NAME1 = "TestDB1";
    private static final String DB_NAME2 = "TestDB2";

    @BeforeClass
    public void setup() {
        result = BCompileUtil.compile("balfiles/transaction/sql_xa_transaction_test.bal");
        SQLDBUtils.deleteFiles(new File(SQLDBUtils.DB_DIRECTORY_H2_1), DB_NAME1);
        SQLDBUtils.deleteFiles(new File(SQLDBUtils.DB_DIRECTORY_H2_2), DB_NAME2);
        SQLDBUtils
                .initH2Database(SQLDBUtils.DB_DIRECTORY_H2_1, DB_NAME1, "sqlfiles/SQLH2CustomerTableCreate.sql");
        SQLDBUtils.initH2Database(SQLDBUtils.DB_DIRECTORY_H2_2, DB_NAME2, "sqlfiles/SQLH2SalaryTableCreate.sql");
    }

    @Test
    public void testXATransactonSuccess() {
        BValue[] returns = BRunUtil.invoke(result, "testXATransactonSuccess");
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 1);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 1);
    }

    @Test
    public void testXATransactonSuccessWithDataSource() {
        BValue[] returns = BRunUtil.invoke(result, "testXATransactonSuccessWithDataSource");
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 1);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 1);
    }

    @Test
    public void testXATransactonFailed1() {
        BValue[] returns = BRunUtil.invoke(result, "testXATransactonFailed1");
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 0);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 0);
    }

    @Test
    public void testXATransactonFailed2() {
        BValue[] returns = BRunUtil.invoke(result, "testXATransactonFailed2");
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 0);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 0);
    }

    @Test
    public void testXATransactonRetry() {
        BValue[] returns = BRunUtil.invoke(result, "testXATransactonRetry");
        Assert.assertEquals(((BInteger) returns[0]).intValue(), 1);
        Assert.assertEquals(((BInteger) returns[1]).intValue(), 1);
    }

    @AfterSuite
    public void cleanup() {
        SQLDBUtils.deleteDirectory(new File(SQLDBUtils.DB_DIRECTORY_H2_1));
        SQLDBUtils.deleteDirectory(new File(SQLDBUtils.DB_DIRECTORY_H2_2));
    }
}
