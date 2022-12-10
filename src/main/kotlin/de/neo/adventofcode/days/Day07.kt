package de.neo.adventofcode.days

class Day07 : IDay {

    inner class FileDescriptor(val name: String, private val parent: FileDescriptor? = rootFileDescriptor) {

        private val children = mutableMapOf<String, FileDescriptor>()

        var size = 0
            set(value) {
                if (parent != null) {
                    parent.size += value - field
                }
                field = value
            }

        val isDir: Boolean
            get() = children.isNotEmpty()

        fun getOrCreateChild(name: String): FileDescriptor {
            if (!children.contains(name)) {
                children[name] = FileDescriptor(name, this)
            }
            return children[name]!!
        }

        fun resolveFileDescriptor(path: List<String>): FileDescriptor {
            var descriptor = this
            path.forEach { descriptor = descriptor.getOrCreateChild(it) }
            return descriptor
        }

        fun getChildDescriptors(): List<FileDescriptor> {
            return children.values.toList()
        }

    }

    private val TOTAL_SPACE = 70_000_000
    private val UPDATE_SIZE = 30_000_000

    private val rootFileDescriptor = FileDescriptor("/", null)

    override fun common(input: Array<String>) {
        val currentDirectory = mutableListOf<String>()
        input.forEach {
            if (it.startsWith("$")) {
                it.split(" ").let { args ->
                    if (args.size == 3) {
                        if (args[2] == "/") currentDirectory.clear()
                        else if (args[2] == "..") currentDirectory.removeLast()
                        else currentDirectory.add(args[2])
                    }
                }
            } else {
                it.split(" ").let { args ->
                    if (args.size == 2) {
                        val size = args[0].toIntOrNull() ?: 0
                        val name = args[1]
                        rootFileDescriptor.resolveFileDescriptor(currentDirectory).getOrCreateChild(name).size = size
                    }
                }
            }
        }
    }

    override fun part01(): String {
        var totalSize = 0
        var calculator: ((List<FileDescriptor>, String) -> Unit)? = null
        calculator = { tree, prefix ->
            tree.forEach {
                /*
                val indentation = "  ".repeat(prefix.count { it == '/' })
                val descriptorInfo = if (it.isDir) "dir" else "file, size=${it.size}"
                println("$indentation- ${it.name} ($descriptorInfo)")
                 */
                if (it.isDir) {
                    if (it.size <= 100_000) totalSize += it.size
                    calculator!!.invoke(it.getChildDescriptors(), "$prefix/${it.name}")
                }
            }
        }
        calculator.invoke(rootFileDescriptor.getChildDescriptors(), "")
        return totalSize.toString()
    }

    override fun part02(): String {
        val spaceFree = TOTAL_SPACE - rootFileDescriptor.size
        val spaceNeeded = UPDATE_SIZE - spaceFree
        var smallestDir = TOTAL_SPACE // use total space as placeholder
        var findSmallestDir: ((List<FileDescriptor>) -> Unit)? = null
        findSmallestDir = { tree ->
            tree.forEach { descriptor ->
                findSmallestDir!!.invoke(descriptor.getChildDescriptors().filter { it.isDir && it.size >= spaceNeeded })
                if (descriptor.size < smallestDir) smallestDir = descriptor.size
            }
        }
        findSmallestDir.invoke(rootFileDescriptor.getChildDescriptors().filter { it.isDir && it.size >= spaceNeeded })
        return smallestDir.toString()
    }

}