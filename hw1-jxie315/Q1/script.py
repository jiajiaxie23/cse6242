import http.client
import json
import time
import timeit
import sys
import json
import collections
import getopt
from pygexf.gexf import Gexf



key=str(sys.argv[1])#global variable which input from the command line
#
# implement your data retrieval code here
#


url_lego="/api/v3/lego/sets/?key="
url_sorting_lego="&page=1&page_size=1000&ordering=-num_parts%2Cid&min_parts="
  
host="rebrickable.com"

connector = http.client.HTTPSConnection(host)
  
connector.request("GET",url_lego+key+url_sorting_lego+str(1155))
outcome_lego= connector.getresponse()
print(outcome_lego.status,outcome_lego.reason) #check the status, 200 means ok
  
  

  
data_lego=json.loads(outcome_lego.read())


data_lego_results=data_lego['results']

data_specific=[]

for a in data_lego_results:
    inted={}
    inted['name'] = a['name']
    inted['set_num']=a['set_num']
    data_specific.append(inted)

print(data_specific) #This is the lego set we are looking for



url1="/api/v3/lego/sets/"
url2="/parts/?key="
url_sorting="&page=1&page_size=1000"




data_parts={}
for a in data_specific:
  connector.request("GET",url1+a['set_num']+url2+key+url_sorting)
  outcome= connector.getresponse()
  print(outcome.status,outcome.reason) #check the status, 200 means ok
  
                

  

   # This will be a list
   # This is a list also
  data_selected=sorted(json.loads(outcome.read())['results'], key = lambda i:i['quantity'], reverse=True)[0:20]




  carrier2={}
  counter=1
  for elem in data_selected:
    carrier={}
    carrier['part_num']=elem['part']['part_num']
    carrier['color'] = elem['color']['rgb']
    carrier['part_name']=elem['part']['name']
    carrier['quantity']=elem['quantity']
    carrier['id']=elem['part']['part_num']+'_'+elem['color']['rgb']
    carrier2[counter]=carrier
    counter=counter+1




  data_parts[a['set_num']]= carrier2#This will be the final step for each loop

print(data_parts)
print(len(data_parts))





# complete auto grader functions for Q1.1.b,d
def min_parts():
    """
    Returns an integer value
    """
    # you must replace this with your own value
    return 1155

def lego_sets():
    """
    return a list of lego sets.
    this may be a list of any type of values
    but each value should represent one set

    e.g.,
    biggest_lego_sets = lego_sets()
    print(len(biggest_lego_sets))
    > 280
    e.g., len(my_sets)
    """
  
  
    data_test=data_specific



  
  

  
    print(data_test)
    print("The size of the data is: ",len(data_test))
    
    
    
    # you must replace this line and return your own list
    return data_test

def gexf_graph():
    """
    return the completed Gexf graph object
    """
    # you must replace these lines and supply your own graph
    
    
    
    my_gexf = Gexf("JiajiaXie", "My awesome graph")
    graph=my_gexf.addGraph("undirected", "static", "My awesome networks")
    
    atr1=graph.addNodeAttribute('Type',type='string')


    for set in data_specific:
      if graph.nodeExists(set['set_num']) ==0:
        tm1=graph.addNode(set['set_num'], set['name'], r='0', g='0', b='0')
        tm1.addAttribute(atr1,"set")



    counter_test=1
    for set, part in data_parts.items():
        for key, part_list in part.items():
          interme =part_list['color']
          red=interme[0]+interme[1]
          green=interme[2]+interme[3]
          blue=interme[4]+interme[5]

          red_de=str(int(red,16))
          green_de=str(int(green,16))
          blue_de=str(int(blue,16))
          if graph.nodeExists(part_list['id'])==0:
            tm2=graph.addNode(part_list['id'], part_list['part_name'],r=red_de, g=green_de, b = blue_de)
            tm2.addAttribute(atr1,"part")


          counter_test+=1
          graph.addEdge("_"+str(counter_test), set, part_list['id'], part_list['quantity'])



    f=open('bricks_graph.gexf','wb')
    my_gexf.write(f)


    return my_gexf.graphs[0]

def avg_node_degree():
    """
    hardcode and return the average node degree
    (run the function called “Average Degree”) within Gephi
    """
    # you must replace this value with the avg node degree
    return 5.373

def graph_diameter():
    """
    hardcode and return the diameter of the graph
    (run the function called “Network Diameter”) within Gephi
    """
    # you must replace this value with the graph diameter
    return 8

def avg_path_length():
    """
    hardcode and return the average path length
    (run the function called “Avg. Path Length”) within Gephi
    :return:
    """
    # you must replace this value with the avg path length
    return 4.424


if __name__ =="__main__":
    graph=gexf_graph()



