package de.neo.adventofcode.days

class Day07 : IDay {

    val fileTree = mutableMapOf<String, Int>()

    override fun common(input: Array<String>) {
        var currentDirectory = ""
        input.forEach {
            if (it.startsWith("$ ")) {
                val cmd = it.substring(2)
                if (cmd.startsWith("cd ")) {
                    val directoryParts = currentDirectory.split("/")
                    val lastPart = directoryParts[directoryParts.size - 1]
                    when (cmd) {
                        "cd /" -> currentDirectory = "/"

                        "cd sgt" -> {
                            println("sgt detect: '$currentDirectory'")
                            currentDirectory += "${if (currentDirectory.endsWith("/")) "" else "/"}${cmd.substring(3)}"

                        }

                        "cd .." -> {
                            currentDirectory = currentDirectory.replace("/$lastPart", "")
                        }

                        else -> {
                            currentDirectory += "${if (currentDirectory.endsWith("/")) "" else "/"}${cmd.substring(3)}"
                        }
                    }
                    println("change to '$currentDirectory'")
                } else if (cmd == "ls") {}
            } else {
                val fileInfo = it.split(" ").toMutableList()
                if (fileInfo[0] == "dir") {
                    fileInfo[0] = "0"
                }
                val slashNeeded = !currentDirectory.endsWith("/")
                fileTree["$currentDirectory${if (slashNeeded) "/" else ""}${fileInfo[1]}"] = fileInfo[0].toInt()
            }
        }
    }

    override fun part01(): String {
        val sizes = mutableMapOf<String, Int>()
        fileTree.filter { it.value == 0 }.forEach { sizes[it.key] = it.value }
        sizes.forEach {  sizeEntry ->
            println("sz: $sizeEntry")
            if (fileTree[sizeEntry.key] != 0) {
                return@forEach
            }
            sizes[sizeEntry.key] = getSize(fileTree.filter { it.key.startsWith("${sizeEntry.key}/") }, "${sizeEntry.key}/")
        }
        sizes.filter { fileTree[it.key] == 0 && it.value <= 100_000 }.forEach {
            //println(it)
        }
        return sizes.filter { fileTree[it.key] == 0 && it.value <= 100_000 }.map { it.value }.sum().toString()
    }

    fun getSize(tree: Map<String, Int>, filter: String): Int {
        var totalSize = 0
        println("--------------------")
        println(tree)
        tree.forEach { treeEntry ->
            if (treeEntry.key.contains("sgt")) {
                print("")
            }
            println(treeEntry)
            fileTree[treeEntry.key] = if (treeEntry.value == 0) {
                println("===================")
                println("${treeEntry.key}/")
                println(fileTree.filter { it.key.startsWith("${treeEntry.key}") })
                getSize(fileTree.filter { it.key.startsWith("${treeEntry.key}/") && it.value != 0 }, "${treeEntry.key}/")
            } else {
                treeEntry.value
            }
            totalSize += fileTree[treeEntry.key]!!
        }
        println(totalSize)
        println("----------------")
        return totalSize
    }

    override fun part02(): String {
        return ""
    }

}