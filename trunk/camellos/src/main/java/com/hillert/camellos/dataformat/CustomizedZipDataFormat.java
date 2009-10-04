package com.hillert.camellos.dataformat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileMessage;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.util.ExchangeHelper;
import org.apache.camel.util.IOHelper;

/**
 * Custom data format for zipping up files in a way that a standard archive application
 * can extract the archived file.
 *
 * @author Gunnar Hillert
 * @version $Id$
 *
 */
public class CustomizedZipDataFormat implements DataFormat {

        private final int compressionLevel;

        //~~~~~Constructors~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public CustomizedZipDataFormat() {
            this.compressionLevel = Deflater.BEST_SPEED;
        }

        public CustomizedZipDataFormat(int compressionLevel) {
            this.compressionLevel = compressionLevel;
        }

        //~~~~Methods~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        /** Unmarhalling the Zip data. */
        public void marshal(final Exchange exchange, final Object graph,
                            final OutputStream stream) throws Exception {

            final Message message = exchange.getIn();
            final File file = message.getBody(File.class);
            final String fileName = file.getName();

            final InputStream is = exchange.getContext().getTypeConverter().convertTo(InputStream.class, graph);

            final ZipOutputStream zipOutput = new ZipOutputStream(stream);
            zipOutput.setLevel(compressionLevel);

            try {
                zipOutput.putNextEntry(new ZipEntry(fileName));
            } catch (IOException e) {
                throw new IllegalStateException("Error while adding a new Zip File Entry (File name: '"
                                    + fileName +"').", e);
            }

            IOHelper.copy(is, zipOutput);
            zipOutput.close();

        }

        /** Unmarhalling the Zip data. */
        public Object unmarshal(final Exchange exchange, final InputStream stream) throws Exception {

            final InputStream is = ExchangeHelper.getMandatoryInBody(exchange, InputStream.class);
            final ZipInputStream unzipInput = new ZipInputStream(is);

            int i = 1;

            // Create an expandable byte array to hold the inflated data
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();


            try {

                while (unzipInput.getNextEntry() != null) {

                    if (i > 1) {
                        throw new IllegalStateException("Zip file has more than 1 file entry.");
                    }

                    IOHelper.copy(unzipInput, bos);

                    i++;

                }

                return bos.toByteArray();
            } finally {
                unzipInput.close();
                bos.close();
            }

        }

}
