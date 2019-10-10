package me.camdenorrb.sweetsqlj.base.codec;

import me.camdenorrb.sweetsqlj.base.codec.parts.SqlFieldPuller;
import me.camdenorrb.sweetsqlj.base.codec.parts.SqlFieldPusher;


public interface SqlFieldCodec<T> extends SqlFieldPuller<T>, SqlFieldPusher<T> {}