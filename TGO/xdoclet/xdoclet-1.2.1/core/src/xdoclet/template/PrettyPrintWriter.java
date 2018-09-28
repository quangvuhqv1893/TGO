/*
 * Copyright (c) 2001, 2002 The XDoclet team
 * All rights reserved.
 */
package xdoclet.template;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Extends the PrintWriter class by adding the possibility of emiting empty lines.
 *
 * @author    Andreas "Mad" Schaefer (andreas.schaefer@madplane.com)
 * @author    Ara Abrahamian (ara_e@email.com)
 * @created   March 7, 2001
 * @version   $Revision: 1.5 $
 */
public class PrettyPrintWriter extends PrintWriter
{
    /**
     * Convenience variable for printing and matching line separators in a system portable manner.
     */
    public final static String LINE_SEPARATOR = "\n";
    // System.getProperty( "line.separator" );
    public final static int LINE_SEPARATOR_LEN = LINE_SEPARATOR.length();

    private boolean prevCharWasNewLine = false;
    private StringBuffer lineBuffer = new StringBuffer();
    private int     lineBufferLength;

    /**
     * Describe what the PrettyPrintWriter constructor does
     *
     * @param pOut  Describe what the parameter does
     */
    public PrettyPrintWriter(OutputStream pOut)
    {
        super(pOut);
    }

    /**
     * Describe what the PrettyPrintWriter constructor does
     *
     * @param pOut  Describe what the parameter does
     */
    public PrettyPrintWriter(Writer pOut)
    {
        super(pOut);
    }


    /**
     * Describe what the PrettyPrintWriter constructor does
     *
     * @param pOut        Describe what the parameter does
     * @param pAutoFlush  Describe what the parameter does
     */
    public PrettyPrintWriter(Writer pOut, boolean pAutoFlush)
    {
        super(pOut, pAutoFlush);
    }

    /**
     * Closes the output stream and writes the last line.
     */
    public void close()
    {
        if (lineBuffer.length() > 0) {
            writeLine();
        }

        super.close();
    }

    /**
     * Describe what the method does
     *
     * @param pBuffer  Describe what the parameter does
     * @param pOffset  Describe what the parameter does
     * @param pLength  Describe what the parameter does
     */
    public void write(char pBuffer[], int pOffset, int pLength)
    {
        for (int i = pOffset; i < pOffset + pLength; i++) {
            char c = pBuffer[i];

            if (c != 13) {
                if (c == 10) {
                    lineBuffer.append(LINE_SEPARATOR);
                    lineBufferLength += LINE_SEPARATOR_LEN;

                    writeLine();
                }
                else {
                    lineBuffer.append(c);
                    lineBufferLength++;
                }
            }
        }
    }

    public void write(int c)
    {
        if (c != 13) {
            lineBuffer.append(c);
            lineBufferLength++;

            if (c == 10) {
                writeLine();
            }
        }
    }

    /**
     * Describe what the method does
     *
     * @param pText    Describe what the parameter does
     * @param pOffset  Describe what the parameter does
     * @param pLength  Describe what the parameter does
     */
    public void write(String pText, int pOffset, int pLength)
    {
        this.write(pText.toCharArray(), pOffset, pLength);
    }

    public void println()
    {
        write(LINE_SEPARATOR);
    }

    protected void writeLine()
    {
        boolean allSpaces = true;
        char tempChar;

        if (lineBufferLength == 1 && lineBuffer.charAt(0) == 10) {
            if (prevCharWasNewLine == false) {
                allSpaces = false;
                prevCharWasNewLine = true;
            }
        }
//    else if( lineBufferLength == 2 && lineBuffer.charAt( 0 ) == 10 && lineBuffer.charAt( 1 ) == 13 )
//    {
//       allSpaces = false;
//    }
        else {
            for (int j = 0; j < lineBufferLength; j++) {
                tempChar = lineBuffer.charAt(j);

                if (!Character.isWhitespace(tempChar)) {
                    prevCharWasNewLine = false;
                    allSpaces = false;
                    break;
                }
            }
        }

        if (allSpaces == false) {
            // if a useful line then write it, otherwise ignore garbage lines
            char[] lineChars = lineBuffer.toString().toCharArray();

            super.write(lineChars, 0, lineChars.length);
        }
        lineBuffer = new StringBuffer();

        // reset line buffer
        lineBufferLength = 0;
    }
}
