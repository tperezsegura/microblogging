package org.personal.domain.exception

class SelfFollowNotAllowedException : RuntimeException("A user cannot follow themself")