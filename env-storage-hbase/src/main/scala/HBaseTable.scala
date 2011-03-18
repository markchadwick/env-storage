package env.storage.hbase

import org.apache.hadoop.hbase.client.HTable

import env.storage.memory.MemoryTable

class HBaseTable(name: String, htable: HTable) extends MemoryTable(name) {
}
