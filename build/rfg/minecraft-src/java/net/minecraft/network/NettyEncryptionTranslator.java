package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator
{
    private final Cipher field_150507_a;
    private byte[] field_150505_b = new byte[0];
    private byte[] field_150506_c = new byte[0];
    private static final String __OBFID = "CL_00001237";

    protected NettyEncryptionTranslator(Cipher cipherIn)
    {
        this.field_150507_a = cipherIn;
    }

    private byte[] func_150502_a(ByteBuf p_150502_1_)
    {
        int i = p_150502_1_.readableBytes();

        if (this.field_150505_b.length < i)
        {
            this.field_150505_b = new byte[i];
        }

        p_150502_1_.readBytes(this.field_150505_b, 0, i);
        return this.field_150505_b;
    }

    protected ByteBuf func_150503_a(ChannelHandlerContext ctx, ByteBuf buffer) throws ShortBufferException
    {
        int i = buffer.readableBytes();
        byte[] abyte = this.func_150502_a(buffer);
        ByteBuf bytebuf1 = ctx.alloc().heapBuffer(this.field_150507_a.getOutputSize(i));
        bytebuf1.writerIndex(this.field_150507_a.update(abyte, 0, i, bytebuf1.array(), bytebuf1.arrayOffset()));
        return bytebuf1;
    }

    protected void func_150504_a(ByteBuf p_150504_1_, ByteBuf p_150504_2_) throws ShortBufferException
    {
        int i = p_150504_1_.readableBytes();
        byte[] abyte = this.func_150502_a(p_150504_1_);
        int j = this.field_150507_a.getOutputSize(i);

        if (this.field_150506_c.length < j)
        {
            this.field_150506_c = new byte[j];
        }

        p_150504_2_.writeBytes(this.field_150506_c, 0, this.field_150507_a.update(abyte, 0, i, this.field_150506_c));
    }
}