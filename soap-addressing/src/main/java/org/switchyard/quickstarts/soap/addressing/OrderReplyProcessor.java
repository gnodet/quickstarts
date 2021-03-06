/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */

package org.switchyard.quickstarts.soap.addressing;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;
import javax.xml.soap.SOAPHeaderElement;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.w3c.dom.Element;

@Named("orderReplyProcessor")
public class OrderReplyProcessor implements Processor {

    /**
     * Creates new processor.
     */
    public OrderReplyProcessor() {
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        OrderResponse response = new OrderResponse();
        response.setReturn("Thank you for your order. You should hear back from our WarehouseService shortly!");
        exchange.getOut().setBody(response);
    }

}
