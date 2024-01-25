package kr.jeongsejong.core.persistence.spec

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EncryptedSupplement

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlainDefault