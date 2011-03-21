Env Storage
===========
Env Storage is a little library which lets you fiddle with sorted maps without
worrying (quite yet) about the exact implementation. It is a small interface
that lets you build up your application without settling on a backend, and
leaving the door open to change your mind later. In fact, if this abstraction
doesn't stick with you, it strives to make it easy to leave it in the dust
entirely.

This software is currently "alpha" quality, and poking around should be done
with that in mind. Trying to push it to a production environment would be, in a
word, discouraged.

Basic Syntax
------------
This library exposes various backends to appear as a Scala
[SortedMap](http://www.scala-lang.org/api/current/scala/collection/SortedMap.html).
A simple dialog might be as follows.

    > import env.storage.memory.MemoryStorage
    > val storage = new MemoryStorage()
    > val counts = storage.getOrCreateTable("counts")
    >

Implementations
---------------
At present, there are only to backend impelementations to `env-storage`; "In
Memory" and "HBase." Neither would be suitable for a production environment at
the moment.
