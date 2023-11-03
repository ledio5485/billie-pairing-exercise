package io.billie.common.util

inline fun <reified T : Enum<T>> fromEnum(enum: Enum<*>): T = enumValueOf<T>(enum.name)
