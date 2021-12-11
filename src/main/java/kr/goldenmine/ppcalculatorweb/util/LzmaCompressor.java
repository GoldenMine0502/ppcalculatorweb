package kr.goldenmine.ppcalculatorweb.util;

import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import lzma.streams.LzmaOutputStream;

import java.io.*;
import java.nio.file.Path;

public class LzmaCompressor
{
    private File rawFile;
    private File compressedFile;

    public LzmaCompressor(File rawFile, File compressedFile)
    {
        this.rawFile = rawFile;
        this.compressedFile = compressedFile;
    }

    public void compress() throws IOException
    {
        try (LzmaOutputStream outputStream = new LzmaOutputStream.Builder(
                new BufferedOutputStream(new FileOutputStream(compressedFile)))
                .useMaximalDictionarySize()
                .useMaximalFastBytes()
                .build();
             InputStream inputStream = new BufferedInputStream(new FileInputStream(rawFile)))
        {
            copy(inputStream, outputStream);
        }
    }

    public void decompress() throws IOException
    {
        try (LzmaInputStream inputStream = new LzmaInputStream(
                new BufferedInputStream(new FileInputStream(compressedFile)),
                new Decoder());
             OutputStream outputStream = new BufferedOutputStream(
                     new FileOutputStream(rawFile)))
        {
            copy(inputStream, outputStream);
        }
    }

    public void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buf = new byte[1024];
        int readData;

        while ((readData = input.read(buf)) > 0) {
            output.write(buf, 0, readData);
        }

        input.close();
        output.close();
    }
}