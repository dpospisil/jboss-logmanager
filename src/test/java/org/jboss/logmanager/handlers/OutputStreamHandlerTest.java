/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.logmanager.handlers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import static org.hamcrest.core.Is.is;

import org.jboss.logmanager.ExtLogRecord;
import org.jboss.logmanager.handlers.ConsoleHandler.Target;
import org.junit.After;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class OutputStreamHandlerTest {

    private StringWriter out;
    private OutputStreamHandler handler;

    private static final Formatter NO_FORMATTER = new Formatter() {
        public String format(final LogRecord record) {
            return record.getMessage();
        }
    };

    public OutputStreamHandlerTest() {
    }

    @Before
    public void prepareBuffer() {
        out = new StringWriter();
    }

    @After
    public void cleanAll() throws IOException {
       handler.flush();
       handler.close();
       out.close();
    }

    @Test
    public void testSetEncoding() throws Exception {
        handler = new OutputStreamHandler();
        handler.setEncoding("UTF-8");
        assertThat(handler.getEncoding(), is("UTF-8"));
    }

   @Test
    public void testSetEncodingOnOutputStream() throws Exception {
       handler = new ConsoleHandler(Target.SYSTEM_OUT, NO_FORMATTER);
        handler.setWriter(out);
        handler.setEncoding("UTF-8");
        assertThat(handler.getEncoding(), is("UTF-8"));
        handler.publish(createLogRecord("Hello World"));
        assertThat(out.toString(), is("Hello World"));
    }

    @Test
    public void testSetNullEncodingOnOutputStream() throws Exception {
        handler = new OutputStreamHandler(NO_FORMATTER);
        handler.setWriter(out);
        handler.setEncoding(null);
        handler.publish(createLogRecord("Hello World"));
        assertThat(out.toString(), is("Hello World"));
    }

    protected ExtLogRecord createLogRecord(final String msg) {
        return new ExtLogRecord(Level.INFO, msg, getClass().getName());
    }


}
