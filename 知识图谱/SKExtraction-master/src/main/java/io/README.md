### 属性定义
- API_name,表示这段文本描述的API，或者是从哪个API的描述中抽取出来的。最好是一个完整的API的绝对路径名，比如类是带包名的，方法是带包名类名和参数类型的，形参名字可以省略。
- API_Type，API的类型，例如library、package、class、method、constant
    - library
    - package
    - method
    - class
    - constant
    - 。。。
- parent_API_name，这个API属于的父API的名字，方便建立API的之间的关系，比如一个常量的父API的是某个API类。可以不存在。
- text_title，接下来的文本是从哪种类型的描述文本抽取的,或者说主宰这段文本的标题，不写的就是默认的description,其他的可以是Overrides、Parameters、Returns、throws，主要针对的是具体到方法的描述，会存在子标题。下面的是
    - Overrides
    - Parameters
    - Returns
    - throws
    - 。。。
- sub_title，可以省略，比如在参数说明（Parameters）段的文字中，通常会涉及几个参数。前面是参数名，后面是对应参数的说明。这里是进行更加细的划分。
以下面的第一行举例，sub_title="start",sub_title_index="1",text="The beginning index, inclusive."
    例子。
    ```$xslt
        start - The beginning index, inclusive.
        end - The ending index, exclusive.
        str - String that will replace previous contents.
    ```

- sub_title_index,sub_title的先后顺序有时候是有关系的，比如在描述方法的参数时，可以用来辅助定位当前的参数时方法签名的哪一个参数，取值从1开始。
- text，文本字段。必须存在的。所有其他附加信息都可以不存在。所有之后的抽取都对应这这个文本。

- sentence_index,假如后面的text对应文本时经过分句之后的文本，且只是原来完整的描述的某一句话话。利用这个就能够表明之后的文本时属于哪一句话。下标从1开始。这个主要时保留句子的下标特征，可能在某些分析中句子的先后顺序是很重要的信息。可以不存在。
- paragraph_index,假如后面的text对应文本时经过分段之后的文本，且只是原来完整的描述的某一段话。利用这个就能够表明之后的文本时属于哪一句话。下标从1开始。这个主要时保留句子的下标特征，可能在某些分析中句子的先后顺序是很重要的信息。可以不存在。
- document_index,假如后面的text对应文本来自于同一个文档集合的不同的文档。利用这个就能够表明之后的文本时属于哪一个文档。下标从1开始。这个主要时保留文档级别特征，可能在某些分析中句子的先后顺序是很重要的信息。可以不存在。
比如我们这次抽取针对的是JDK8的文档，其中某一段话是描述java.util.List的类的详细文档，另外一段话来自java.util.Map的文档。document_index就必须不同。这个特征有利于一些根据文档整体特征的分析，比如tf-idf就需要这个特征。
- knowledge_pattern,来自于描述API文档的中句子具有的知识模式的分类，常见的比如Concept、directive、Functionlity and behavior等。来源可以是人工标注的也可以是自动识别的。加入这个字段，照常读取后就可以作为标注数据了。


## 1. 从纯文本格式的数据文件读取
给定一个txt文件，名字加上路径，然后进行逐行读取。
然后生成APIDataSource这个类的对象。
还可以给这个对象加上序列化接口，从而实现可以存到文件再读取出来。使得可以

输入的待抽取的关系的API文本必须以这样的方式组织。
```$xslt
API_full_name%%v1#:API_Type%%v2#:parent_API_full_name%%v3#:text_title%%v4#:sub_title%%v5#:sub_title_index%%v6#:text%%v7 
```
每一段文本前面可以有一些附加信息，是属性名和值组成的对，可以有无数个。
键值对之间通过"#:"来进行分隔，保证最后一个域一定是文本，并且没有属性名。

键值对内部，形式为"Key%%Value",通过"%%"分隔，前者为属性名，后者为属性值。
所有属性不一定需要完全具备
具体支持的属性见上面的属性值定义
## 2. 从序列化后的对象文件读取
利用java的序列化功能。将APIDataSource对象序列化后再反序列化回来。
给定一个一个序列化文件名，名字加上路径。
## 3. 从json文件读取
给定一个json文件，名字加上路径，然后进行读取。
数据以json文件的形式组织，但是格式可能分为两种，一种是冗余版，一种是整合版。

1. 冗余版。
数组里面的每一项都是单独的全体信息
```$xslt
[
{"API_full_name":"xxx",
"API_full_name":"xxx",
"text":"xxx"},
{"API_full_name":"xxx",
"API_full_name":"xxx",
"text":"xxx"}
]
``
2.整合版
按照文档的整体结构，进行嵌套，里层的共享外层的一些属性
比如外层是共享的父API，里面是一个个的子API。




## 4. 从MySQL读取
这个还没有实现。不过不好进行。
指定MySQL的访问地址以及用户名密码之类。给定表和属性列名，然后自动读取所有数据然后进行整合形成APIDataSource对象实例。
（其实可以用其他语言或者在其他项目写？我们的项目只接受txt与json就好？）





