/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License, v.2.1 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.switchyard.quickstarts.demo.policy.transaction;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.switchyard.annotations.Requires;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;
import org.switchyard.policy.TransactionPolicy;

/**
 *  Transactional service implementation. To trigger a rollback on the 
 *  current transaction, pass <code>WorkService.ROLLBACK</code> as the command name.
 */
@Service(WorkService.class)
@Requires(transaction = TransactionPolicy.PROPAGATES_TRANSACTION)
public class WorkServiceBean
    implements org.switchyard.quickstarts.demo.policy.transaction.WorkService {
    
    /** rollback command. */
    public static final String ROLLBACK = "rollback";

    private static final String JNDI_TRANSACTION_MANAGER = "java:jboss/TransactionManager";
    
    @Inject @Reference @Requires(transaction = TransactionPolicy.PROPAGATES_TRANSACTION)
    private TaskAService _taskAService;
    
    @Inject @Reference @Requires(transaction = TransactionPolicy.SUSPENDS_TRANSACTION)
    private TaskBService _taskBService;
    
    @Inject @Reference @Requires(transaction = TransactionPolicy.SUSPENDS_TRANSACTION)
    private TaskCService _taskCService;

    @Override
    public final void doWork(final String command) {
        
        print("Received command =>  " + command);

        Transaction t = null;
        try {
            t = getCurrentTransaction();
        } catch (Exception e) {
            print("Failed to get current transcation");
            return;
        }
        if (t == null) {
            print("No active transaction");
        }
        _taskAService.doTask(command);
        _taskBService.doTask(command);
        _taskCService.doTask(command);

        try {
            t = getCurrentTransaction();
            if (t == null) {
                print("No active transaction");
                return;
            } else if (t.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
                print("transaction is marked as rollback only");
            } else if (t.getStatus() == Status.STATUS_ACTIVE) {
                print("transaction will be committed");
            } else {
                print("Invalid transaction status: " + t.getStatus());
            }
        } catch (Exception e) {
            print("Failed to get current transaction status");
        }
    }
    
    private Transaction getCurrentTransaction() throws Exception {
        TransactionManager tm = (TransactionManager)
                new InitialContext().lookup(JNDI_TRANSACTION_MANAGER);
        return tm.getTransaction();
    }
        
    private void print(String message) {
        System.out.println(":: WorkService :: " + message);
    }
}
